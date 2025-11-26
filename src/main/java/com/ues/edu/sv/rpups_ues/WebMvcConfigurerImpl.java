package com.ues.edu.sv.rpups_ues;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigurerImpl implements WebMvcConfigurer {

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    /**
     * Configuración de CORS para permitir solicitudes desde dominios específicos.
     * Los orígenes permitidos se configuran en application.properties o variables de entorno.
     * 
     * @param registry el registro de CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.split(","))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type", "X-Requested-With", "Accept")
                .exposedHeaders("Content-Disposition", "Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }
}