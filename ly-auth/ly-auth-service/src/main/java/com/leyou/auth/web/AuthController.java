package com.leyou.auth.web;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import com.leyou.user.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆授权
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private JwtProperties prop;
    @Autowired
    private AuthService authService;
    @Value("${ly.jwt.cookieName}")
    private String cookieName;

    @PostMapping("login")
    public ResponseEntity<Void> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpServletResponse response, HttpServletRequest request) {
        //登陆
        String token = authService.login(username, password);
        // 写入cookie
        CookieUtils.newBuilder(response).httpOnly().request(request)
                .build(cookieName, token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 校验用户登录状态
     *
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN") String token,
                                           HttpServletResponse response, HttpServletRequest request) {
        if (StringUtils.isBlank(token)) {
            //如果没有token
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
        try {
            //解析token
            UserInfo info = JwtUtils.getUserInfo(prop.getPublicKey(), token);

            //刷新token，重新生成token。每次操所时，刷新token时间
            String newToken = JwtUtils.generateToken(info, prop.getPrivateKey(), prop.getExpire());
            //写回token
            CookieUtils.newBuilder(response).httpOnly().request(request)
                    .build(cookieName, newToken);



            //一登录，返回用户信息
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            //token已过期，或者token无效
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
    }
}
