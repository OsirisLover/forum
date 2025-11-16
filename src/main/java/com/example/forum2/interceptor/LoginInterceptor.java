package com.example.forum2.interceptor;

import com.example.forum2.config.AppConfig;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取用户session, 并判断用户是否登录
        HttpSession session = request.getSession(false);  //如果存在会话，则返回会话，不存在则返回null
        if (session != null && session.getAttribute(AppConfig.USER_SESSION) != null) {
            // 用户已登录
            return true;
        }
        // 没有登录强制跳转到登录页面
        response.sendRedirect("/sign-in.html");
        return false;
    }
}
