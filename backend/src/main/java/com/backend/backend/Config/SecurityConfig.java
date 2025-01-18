package com.backend.backend.Config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.api.client.util.Value;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;
    
    private String getFrontendUrl(){
        if(frontendUrl == null) return "http://localhost:8050";
        return frontendUrl;
    }

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(CustomOAuth2SuccessHandler customOAuth2SuccessHandler, JwtTokenProvider jwtTokenProvider) {
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    
    @PostConstruct
    public void init() {
        logger.info("Frontend URL: {}", getFrontendUrl());
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        if(frontendUrl == null){
            frontendUrl = "http://localhost:5173";
        }

        System.out.println(frontendUrl);

        http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorizeRequest ->
                authorizeRequest.anyRequest().authenticated())
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl(getFrontendUrl()+"/dashboard", true)
                .successHandler(customOAuth2SuccessHandler))
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // Corrected line

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        logger.info("Frontend from cors: "+ frontendUrl);
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
