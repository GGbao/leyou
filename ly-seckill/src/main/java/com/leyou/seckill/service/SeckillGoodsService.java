package com.leyou.seckill.service;

import com.leyou.seckill.vo.SeckillGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/****
 * @Author:shenkunlin
 * @Date:2019/5/27 12:13
 * @Description:
 *****/
@Slf4j
@Service
public class SeckillGoodsService  {

    @Autowired
    private RedisTemplate redisTemplate;

    /****
     * 根据商品ID查询商品详情
     * @param time:商品秒杀时区
     * @param id:商品ID
     * @return
     */
    public SeckillGoods one(String time, Long id) {
        return (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_"+time).get(id);
    }

    /***
     * 根据时间区间查询秒杀商品列表
     * @param time
     * @return
     */
    public List<SeckillGoods> list(String time) {
        log.info("time:"+time);
        //组装key
        String key = "SeckillGoods_"+time;
        return redisTemplate.boundHashOps(key).values();
    }
}
