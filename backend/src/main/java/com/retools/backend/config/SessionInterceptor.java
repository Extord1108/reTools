package com.retools.backend.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws java.lang.Exception {
    }
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }
    @Override
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
        System.out.println(arg0.getSession().getAttribute("user"));
        //登录和注册放行
        if (arg0.getRequestURI().equals("/user/login") || arg0.getRequestURI().equals("/user/register")) {
            return true;
        }
        //其他请求需要登录
        if (arg0.getSession().getAttribute("user") == null) {
            arg1.setStatus(401);
            arg1.setContentType("application/json;charset=utf-8");
            arg1.setCharacterEncoding("UTF-8");
            arg1.getWriter().write("{\"code\":401,\"msg\":\"请先登录\"}");
            return false;
        }
        return true;
    }
}
