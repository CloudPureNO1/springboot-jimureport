package com.wxm.jimureport.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//采用@WebFilter 注解  Filter 方法，还需要在启动类加上@ServletComponentScan注解
//@WebFilter(urlPatterns = "/*", filterName="cosFilter")
// 采用@Component 或者 配置里配置@bean 可以不用@ServletComponentScan注解
@Component
/** 控制过滤器的级别最高 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CosFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest reqs = (HttpServletRequest) req;
        // 可以使用* 代表所有
        // 设置允许Cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 设置允许跨域请求的方法
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        // 允许跨域请求头包含 x-requested-with,Access-Control-Allow-Origin,content-type,token  其他字符
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Access-Control-Allow-Origin,content-type,token");
        // 允许http://www.xxx.com域（自行设置，这里只做示例）发起跨域请求
        response.setHeader("Access-Control-Allow-Origin", reqs.getHeader("Origin"));
        // 设置在3600秒不需要再发送预校验请求
        response.setHeader("Access-Control-Max-Age", "3600");
        filterChain.doFilter(req,res);
    }
    @Override
    public void init(FilterConfig filterConfig) { }
    @Override
    public void destroy() { }
}
