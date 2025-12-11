package com.ues.edu.sv.rpups_ues;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.ues.edu.sv.rpups_ues.auth.filters.JWTAuthenticationFilter;
import com.ues.edu.sv.rpups_ues.auth.filters.JWTAuthorizationFilter;
import com.ues.edu.sv.rpups_ues.auth.services.JWTService;
import com.ues.edu.sv.rpups_ues.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JWTService jwtService,
            BCryptPasswordEncoder passwordEncoder, AuthenticationConfiguration authenticationConfiguration) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers("/swagger-ui/**", "/bus/v3/api-docs/**");
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                            "/api/v1/**", "/swagger-ui/**", "/bus/v3/api-docs/**", "/v3/api-docs/**",
                            "/api/proyectos/public/**", "/api/usuarios/register", "/api/usuarios/register/**", 
                            "/api/v1/password-reset/request/**", "/api/usuarios/repres-empresa", "/api/usuarios/repres-empresa/**", 
                            "/api/deptos-carreras/deptos/**", "/api/carreras/by-depto/**",
                            "/error")  // Permitir acceso a /error para que los errores se propaguen correctamente
                            .permitAll()
                            .anyRequest().authenticated();
                })
                .addFilter(
                        new JWTAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), jwtService))
                .addFilter(
                        new JWTAuthorizationFilter(authenticationConfiguration.getAuthenticationManager(), jwtService))
                .cors(cors -> cors.configure(httpSecurity))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(401);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\": \"No autorizado\", \"message\": \"" + authException.getMessage() + "\"}");
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setStatus(403);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\": \"Acceso denegado\", \"message\": \"" + accessDeniedException.getMessage() + "\"}");
                    })
                )
                .build();
    }
    /*
     * return httpSecurity.csrf(csrf -> csrf.disable())
     * .authorizeHttpRequests(auth -> auth
     * .anyRequest().permitAll())
     * .build();
     */
}
