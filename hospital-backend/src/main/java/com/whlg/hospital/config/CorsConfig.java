package com.whlg.hospital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * 全局CORS配置
 */
@Configuration
public class CorsConfig {

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost:63342",
            "http://localhost:8080",
            "http://127.0.0.1:63342",
            "http://127.0.0.1:8080",
            "http://127.0.0.1:5500",
            "http://localhost:5500"
    );

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowCredentials(true);
        
        ALLOWED_ORIGINS.forEach(config::addAllowedOrigin);
        
        config.addAllowedHeader("*");
        
        config.addAllowedMethod("*");
        
        config.addExposedHeader("Authorization");
        config.addExposedHeader("token");
        
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
