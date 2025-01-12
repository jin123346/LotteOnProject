package com.lotteon.config;


import com.lotteon.interceptor.AppInfoInterceptor;
import com.lotteon.interceptor.UserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        //  configurer.mediaType("css", MediaType.valueOf("text/css"));
    }

    @Autowired
    private AppInfo appInfo;

    private final UserInterceptor userInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppInfoInterceptor(appInfo));
        registry.addInterceptor(userInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/static/**", "/uploads/**");  // 정적 리소스 경로 제외

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**","/uploads/**")
                .addResourceLocations("classpath:/static/css/")
                .addResourceLocations("file:" + uploadPath)
                .setCachePeriod(0);
    }



}
