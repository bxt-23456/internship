package com.whlg.hospital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局CORS配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许携带凭证
        config.setAllowCredentials(true);
        
        // 允许的源（必须指定具体源，不能用*）
        config.addAllowedOrigin("http://localhost:63342");
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://127.0.0.1:63342");
        config.addAllowedOrigin("http://127.0.0.1:8080");
        
        // 允许所有请求头
        config.addAllowedHeader("*");
        
        // 允许所有HTTP方法
        config.addAllowedMethod("*");
        
        // 暴露响应头
        config.addExposedHeader("Authorization");
        
        // 预检请求缓存时间（秒）
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
