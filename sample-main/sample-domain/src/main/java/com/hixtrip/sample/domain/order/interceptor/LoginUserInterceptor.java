package com.hixtrip.sample.domain.order.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;



/**
 * @Description:
 * @author: MingYHua
 * @createTime: 2024-04-13
 **/
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/command/order/create", uri);
        boolean match1 = antPathMatcher.match("/command/order/pay/callback", uri);
        if (match || match1) {
            return true;
        }

        //获取登录的用户信息
        //MemberResponseVo attribute = (MemberResponseVo) request.getSession().getAttribute("LOGIN_USER");
       String attribute = null;
        if (attribute != null) {
            //把登录后用户的信息放在ThreadLocal里面进行保存
            loginUser.set(attribute);

            return true;
        } else {
            //未登录，返回登录页面
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
