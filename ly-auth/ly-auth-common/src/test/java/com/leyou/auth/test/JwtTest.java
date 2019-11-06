package com.leyou.auth.test;


import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author bystander
 * @date 2018/10/1
 */
public class JwtTest {

    private static final String publicKeyPath = "C:\\tmp\\rsa\\rsa.pub";
    private static final String privateKeyPath ="C:\\tmp\\rsa\\rsa.pri";

    private PrivateKey privateKey;
    private PublicKey publicKey;


    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(publicKeyPath, privateKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        privateKey = RsaUtils.getPrivateKey(privateKeyPath);
        publicKey = RsaUtils.getPublicKey(publicKeyPath);
    }

    @org.junit.Test
    public void generateToken() {
        //生成Token
        String s = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("s = " + s);
    }


    @org.junit.Test
    public void parseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU3MjUwODc0Nn0.VuEX01asw6xbd2rhIuq0VkKOiLorT-Vm8ao6DfjeMcJk2plIR44T1p5WqXC6gApzLti5mxyoY4dN1VpN6NYM1yUIpy1X-yB49Jm1WoHNFj0wPqJYd3ulw7G2cBzL0Vedi8Bhf2tAR4adGpybUlUQqHnRMpRLPH4Qrl2Rv3rKn8M";
        UserInfo userInfo = JwtUtils.getUserInfo(publicKey, token);
        System.out.println("id:" + userInfo.getId());
        System.out.println("name:" + userInfo.getUsername());
    }

    @org.junit.Test
    public void parseToken1() {
    }

    @org.junit.Test
    public void getUserInfo() {
    }

    @org.junit.Test
    public void getUserInfo1() {
    }
}