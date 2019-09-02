package com.leyou.order.interceptor;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.order.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    private JwtProperties prop;

    public static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    public UserInterceptor(JwtProperties prop) {
        this.prop = prop;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie
        String token = CookieUtils.getCookieValue(request, prop.getCookieName());
        try {
            //解析token
            UserInfo user = JwtUtils.getUserInfo(prop.getPublicKey(), token);
            //传递user
            tl.set(user);
            //放行
            return true;
        } catch (Exception e) {
            log.error("[订单服务] 解析用户身份失败。", e);

            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //用完数据要删除
        tl.remove();
    }

    public static UserInfo getUser() {
        return tl.get();
    }
}
