package com.ues.edu.sv.rpups_ues;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigurerImpl implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://rpups-ues-frontend.web.app",
                        "http://localhost:3000", // Para desarrollo local
                        "http://127.0.0.1:3000" // Otra posible configuración local
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}