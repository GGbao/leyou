package com.leyou.seckill.web;

import com.github.wxpay.sdk.WXPayUtil;
import com.leyou.auth.entity.UserInfo;
import com.leyou.seckill.interceptor.UserInterceptor;
import com.leyou.seckill.pay.WeixinPayService;
import com.leyou.seckill.service.SeckillOrderService;
import com.leyou.seckill.vo.SeckillOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/pay")
public class PayController {


    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    /**
     * 生成二维码
     *
     * @return
     */
    @GetMapping("/createNative")
    public Map createNative(String orderId) {
        UserInfo user = UserInterceptor.getUser();
        log.info("用户名："+user.getUsername());
        String username = user.getUsername();
        SeckillOrder seckillOrder = seckillOrderService.queryByUserName(username);
        if (seckillOrder != null) {
            //校验该订单是否时当前用户的订单，并且时未支付的订单
            if ("0".equals(seckillOrder.getStatus()) && "0".equals(seckillOrder.getStatus()) && username.equals(seckillOrder.getUserId())) {
                return weixinPayService.createNative(
                        seckillOrder.getId().toString(),
                        (int) (seckillOrder.getMoney().floatValue() * 100),
                        "http://api.leyou.com/api/seckill/pay/notify", username);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 通知
     *
     * @return
     */
    @RequestMapping("/notify")
    public Map notifyLogic(HttpServletRequest request) {
        System.out.println("回调了.....");
        InputStream inStream;
        try {
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
            System.out.println(result);

            Map<String, String> map = WXPayUtil.xmlToMap(result);
            //获取用户名
            String username = map.get("attach");
            seckillOrderService.updateStatus(map.get("out_trade_no"), username, map.get("transaction_id"));//更新订单状态

        } catch (Exception e) {
            e.printStackTrace();
            //记录错误日志
        }
        return new HashMap();
    }


    @GetMapping("/queryPayStatus")
    public Map queryPayStatus(String orderId) {
        return weixinPayService.queryPayStatus(orderId);
    }

}
