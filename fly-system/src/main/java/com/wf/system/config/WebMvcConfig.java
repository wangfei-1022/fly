package com.wf.system.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@Slf4j
public class WebMvcConfig extends WebMvcConfigurationSupport {


    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor;


    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/system/user/login")
                .excludePathPatterns("/system/user/logout")
                .excludePathPatterns("/system/user/getInfo")
                .excludePathPatterns("/system/user/getRouters")
                .excludePathPatterns("/system/user/getCaptcha");


    }
}