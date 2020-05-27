package com.leyou.seckill.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ly.pay")
public class WeiXinPayProperties {
    private String appid;

    private String partner;

    private String partnerkey;

    private String notifyurl;
}
