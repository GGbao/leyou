package com.leyou.seckill.web;

import com.leyou.auth.entity.UserInfo;
import com.leyou.common.vo.Result;
import com.leyou.seckill.interceptor.UserInterceptor;
import com.leyou.seckill.service.SeckillOrderService;
import com.leyou.seckill.vo.SeckillStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.runtime.Log;

/****
 * @Author:itheima
 * @Date:2019/5/27 17:40
 * @Description:
 *****/
@Slf4j
@RestController
@RequestMapping(value = "/order")
public class SeckillOrderController {


    @Autowired
    private SeckillOrderService seckillOrderService;


    /*****
     * URL:/seckill/order/query
     * 查询用户抢单状态
     * 获取用户名
     * 调用Service查询
     */
    @RequestMapping(value = "/query")
    public Result queryStatus(){
        //获取用户名
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//获取登陆用户
        UserInfo user = UserInterceptor.getUser();
        String username = user.getUsername();
        //用户未登录
        if(username.equals(null)){
            return new Result(403,"用户未登录！");
        }
        try {
            //调用Service查询
            SeckillStatus seckillStatus = seckillOrderService.queryStatus(username);
            if(seckillStatus!=null){
                Result result = new Result(seckillStatus.getStatus(),"抢单状态！");
                result.setOther(seckillStatus);
                return  result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //0:表示抢单失败
            return new Result(0,e.getMessage());
        }
        return new Result(404,"无相关信息！");
    }


    /***
     * URL:/seckill/order/add
     * 用户下单操作
     * @param time:商品所在的时间区间
     * @param id:商品iD
     */
    @RequestMapping(value = "/add")
    public Result add(Long id,String time){
        //获取用户名
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo user = UserInterceptor.getUser();
        String username = user.getUsername();
        //如果用户没登录，则提醒用户登录
        if(username.equals(null)){
            return new Result(403,"未登录，请先登录！");
        }

        try {
            //调用Service下单操作

            Boolean bo = seckillOrderService.add(id, time, username);

            if(bo){
                return new Result(0,"抢单成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //将错误信息返回出去
            return new Result(2,e.getMessage());
        }

        return new Result(1,"抢单失败！");
    }

}
