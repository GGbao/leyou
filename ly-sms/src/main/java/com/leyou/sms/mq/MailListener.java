package com.leyou.sms.mq;


import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class MailListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "mail.verify.code.queue", durable = "true"),
            exchange = @Exchange(name = "ly.mail.exchange", type = ExchangeTypes.TOPIC),
            key = "mail.verify.code"
    ))
    public void listenMail(Map<String,String> msg){
        if (msg == null || msg.size() <= 0){
            //不做处理
            return;
        }
        String mail = msg.get("mail");
        String code = msg.get("code");

        if (StringUtils.isNotBlank(mail) && StringUtils.isNotBlank(code)){
            //发送消息
                MailUtils.sendMail(mail, "您正在申请注册乐优商城，您的注册码是：" + code, "乐优商城注册邮件");
            //不做处理
            return;
        }
    }

}
