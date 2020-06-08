package com.leyou.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.leyou.seckill.mapper.SeckillGoodsMapper;
import com.leyou.seckill.pay.WeixinPayService;
import com.leyou.seckill.vo.SeckillGoods;
import com.leyou.seckill.vo.SeckillOrder;
import com.leyou.seckill.vo.SeckillStatus;
import jdk.nashorn.internal.ir.annotations.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/****
 * @Author:itheima
 * @Description:
 *****/
@Slf4j
@Component
public class OrderMessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    /***
     * 消息监听
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "queue.delay.order.done", durable = "true"),
            exchange = @Exchange(name = "exchange.delay.order.done", type = ExchangeTypes.DIRECT),
            key = "delay"
    ))
    public void onMessage(Message message) {
        String content = new String(message.getBody());
//        System.out.println("监听到的消息：" + content);
        log.info("监听到的消息：" + content);

        //回滚操作
        rollbackOrder(JSON.parseObject(content, SeckillStatus.class));
    }


    /*****
     * 订单回滚操作
     * @param seckillStatus
     */
    public void rollbackOrder(SeckillStatus seckillStatus) {
        if (seckillStatus == null) {
            return;
        }
        //判断Redis中是否存在对应的订单
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(seckillStatus.getUsername());

        //如果存在，开始回滚
        if (seckillOrder != null) {
            log.info("订单ID:"+seckillStatus.getOrderId().toString());
            //1.关闭微信支付
            Map<String, String> map = weixinPayService.closePay(seckillStatus.getOrderId().toString());


            if (map.get("return_code").equals("SUCCESS") && map.get("result_code").equals("SUCCESS")) {
                //2.删除用户订单
                redisTemplate.boundHashOps("SeckillOrder").delete(seckillOrder.getUserId());

                //3.查询出商品数据
                SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + seckillStatus.getTime()).get(seckillStatus.getGoodsId());
                if (goods == null) {
                    //数据库中加载数据
                    goods = seckillGoodsMapper.selectByPrimaryKey(seckillStatus.getGoodsId());
                }

                //4.递增库存  incr
                Long seckillGoodsCount = redisTemplate.boundHashOps("SeckillGoodsCount").increment(seckillStatus.getGoodsId(), 1);
                goods.setStockCount(seckillGoodsCount.intValue());

                //5.将商品数据同步到Redis
                redisTemplate.boundHashOps("SeckillGoods_" + seckillStatus.getTime()).put(seckillStatus.getGoodsId(), goods);
                redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillStatus.getGoodsId()).leftPush(seckillStatus.getGoodsId());
                //6.清理用户抢单排队信息
                //清理重复排队标识
                redisTemplate.boundHashOps("UserQueueCount").delete(seckillStatus.getUsername());

                //清理排队存储信息
                redisTemplate.boundHashOps("UserQueueStatus").delete(seckillStatus.getUsername());
            }
        }
    }
}
