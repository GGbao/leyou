package com.leyou.gateway.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
    private String pubKeyPath;
    private PublicKey publicKey;//公钥
    private String cookieName;

    //对象一旦实例化后，就应该读取公钥和私钥
    @PostConstruct
    public void init() throws Exception {
        //读取公钥私钥
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);

    }
}
