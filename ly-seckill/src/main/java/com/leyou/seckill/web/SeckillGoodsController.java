package com.leyou.seckill.web;

import com.leyou.common.utils.DateUtil;
import com.leyou.seckill.service.SeckillGoodsService;
import com.leyou.seckill.vo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/****
 * @Author:shenkunlin
 * @Date:2019/5/27 12:22
 * @Description:
 *****/
@RestController
@RequestMapping("/goods")
public class SeckillGoodsController {

    @Autowired
    private SeckillGoodsService seckillGoodsService;


    /***
     * URL:/seckill/goods/one
     * 根据商品ID查询商品详情
     * @param time:时间
     * @param id:商品ID
     */
    @GetMapping("/one")
    public SeckillGoods one(String time, Long id){
        return seckillGoodsService.one(time,id);
    }

    /*****
     * URL:/seckill/goods/list
     * 加载对应时区的秒杀商品
     * @param  time:2019052715
     */
    @GetMapping("/list")
    public List<SeckillGoods> list(String time){
        return seckillGoodsService.list(time);
    }


    /****
     * 加载所有时间菜单
     * @return
     */
    @RequestMapping("/menus")
    public List<Date> loadMenus(){
        return DateUtil.getDateMenus();
    }



}
