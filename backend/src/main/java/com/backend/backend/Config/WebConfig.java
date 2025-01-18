package com.backend.backend.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig{

    @Value("${frontend.url}")
    private String frontendUrl;

    private String getFrontendUrl(){
        if(frontendUrl == null) return "http://localhost:8050";
        return frontendUrl;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        if(frontendUrl == null){
            frontendUrl = "http://localhost:8050";
        }

        return new WebMvcConfigurer(){
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**")
                .allowedOrigins(getFrontendUrl())
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
            }
        }; 
    }
}