package com.whlg.hospital.config;

import com.whlg.hospital.interceptor.CrossOriginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置 - 注册拦截器和跨域配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private CrossOriginInterceptor crossOriginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(crossOriginInterceptor);
        
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/user/info", "/user/password", "/user/changePassword", "/user/logout", "/follow/**", "/feedback/**", "/familyMember/**")
                .excludePathPatterns(
                        "/user/register",
                        "/user/login",
                        "/user/sendCode",
                        "/index/**"
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:63342", "http://localhost:8080", "http://127.0.0.1:63342", "http://127.0.0.1:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
