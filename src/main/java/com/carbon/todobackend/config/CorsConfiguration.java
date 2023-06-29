package com.carbon.todobackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/api/**")
                .allowedOrigins(
                        "localhost"
                )
                .allowedHeaders(
                        HttpHeaders.ACCEPT,
                        HttpHeaders.CONTENT_TYPE
                );
    }
}