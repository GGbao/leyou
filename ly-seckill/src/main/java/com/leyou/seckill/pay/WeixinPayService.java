package com.leyou.seckill.pay;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.leyou.common.utils.HttpClient;
import com.leyou.seckill.config.PayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
//@EnableConfigurationProperties(WeiXinPayProperties.class)
public class WeixinPayService {


    //    @Autowired
//    private WeiXinPayProperties pay;
    @Autowired
    private PayConfig pay;
    @Autowired
    private WXPay wxPay;


    /****
     * 关闭订单操作
     * @param outtradeno
     * @return
     */
    public Map closePay(String outtradeno) {
        Map param = new HashMap();
        param.put("return_code", "SUCCESS");
        param.put("result_code", "SUCCESS");
        System.out.println("取消支付返回结果"+param);
        return param;
        /*param.put("appid", pay.getAppid());//公众账号ID
        param.put("mch_id", pay.getPartner());//商户号
        param.put("out_trade_no", outtradeno);//订单号
        param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
        String url = "https://api.mch.weixin.qq.com/pay/closeorder";
        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, pay.getPartnerkey());

            log.info(xmlParam);

            HttpClient client = new HttpClient(url);
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();
            String result = client.getContent();
            //将返回数据解析成Map
            return WXPayUtil.xmlToMap(result);*/
            /*Map<String, String> map = WXPayUtil.xmlToMap(result);
            System.out.println(map);
            return map;*/
      /*  } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/
    }

    public Map createNative(String orderId, Integer money, String notifyUrl, String... attach) {
        //1.创建参数
        Map<String, String> param = new HashMap();//创建参数
        param.put("appid", pay.getAppID());//公众号
        param.put("mch_id", pay.getMchID());//商户号
        param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
        param.put("body", "青橙");//商品描述
        param.put("out_trade_no", orderId);//商户订单号
        param.put("total_fee", money + "");//总金额（分）
        param.put("spbill_create_ip", "127.0.0.1");//IP
        param.put("notify_url", notifyUrl);//暂时随便写一个
        param.put("trade_type", "NATIVE");//交易类型
        if (attach != null && attach.length > 0) {
            param.put("attach", attach[0]);
        }
        try {
            /*//2.发送请求
            String xmlParam = WXPayUtil.generateSignedXml(param, pay.getPartnerkey());
            System.out.println(xmlParam);
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();
            //3.获得结果
            String result = client.getContent();
            System.out.println(result);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            Map<String, String> map = new HashMap();
            map.put("code_url", resultMap.get("code_url"));//支付地址
            map.put("total_fee", money + "");//总金额
            map.put("out_trade_no", orderId);//订单号*/
            //利用wxPay工具，完成下单
            Map<String, String> result = wxPay.unifiedOrder(param);
            //下单成功，获取支付链接
            String url = result.get("code_url");
            Map<String, String> map = new HashMap();
            map.put("code_url", url);//支付地址
            map.put("total_fee", money + "");//总金额
            map.put("out_trade_no", orderId);//订单号
            return map;
        } catch (Exception e) {
            log.error("[微信下单] 创建预交易订单异常失败", e);
            return new HashMap();
        }

    }

    public Map queryPayStatus(String orderId) {
        Map param = new HashMap();
        param.put("appid", pay.getAppID());//公众号
        param.put("mch_id", pay.getMchID());//商户号
        param.put("out_trade_no", orderId);//订单号
        param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, pay.getKey());
            HttpClient client = new HttpClient(url);
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();
            String result = client.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            System.out.println(map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
