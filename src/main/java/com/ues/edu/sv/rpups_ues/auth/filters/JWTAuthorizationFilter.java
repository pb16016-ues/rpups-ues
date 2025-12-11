package com.ues.edu.sv.rpups_ues.auth.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.ues.edu.sv.rpups_ues.auth.services.JWTService;
import com.ues.edu.sv.rpups_ues.auth.services.JWTServiceImpl;

import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final JWTService jwtService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        log.info("========================================");
        log.info("=== JWT Authorization Filter START ===");
        log.info("URI: {} | Method: {}", request.getRequestURI(), request.getMethod());
        
        // Probar ambas variantes del header (case-insensitive según HTTP spec)
        String header = request.getHeader(JWTServiceImpl.HEADER_STRING);
        String headerCaps = request.getHeader("Authorization");
        
        log.info("Header 'authorization': {}", header != null ? "PRESENTE" : "NULL");
        log.info("Header 'Authorization': {}", headerCaps != null ? "PRESENTE" : "NULL");
        
        // Usar el que esté disponible
        String tokenHeader = header != null ? header : headerCaps;
        
        if (tokenHeader != null) {
            log.info("Token (primeros 60 chars): {}", tokenHeader.substring(0, Math.min(60, tokenHeader.length())));
        } else {
            log.warn("NO HAY TOKEN EN EL REQUEST");
        }

        if (!jwtService.requiresAuthentication(tokenHeader)) {
            log.info("Token no presente o formato incorrecto, continuando sin autenticar");
            chain.doFilter(request, response);
            return;
        }
        
        log.info("Token presente con formato Bearer, validando...");

        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            if (jwtService.validate(tokenHeader)) {
                var authorities = jwtService.getAuthorities(tokenHeader);
                log.info("Token VÁLIDO - Authorities: {}", authorities);
                
                authenticationToken = new UsernamePasswordAuthenticationToken(
                    jwtService.getId(tokenHeader), null, authorities);
                log.info("Authentication creada - Principal: {}, Authorities: {}", 
                    authenticationToken.getPrincipal(), authenticationToken.getAuthorities());
            } else {
                log.warn("Token INVÁLIDO");
            }
        } catch (Exception e) {
            log.error("ERROR procesando token: {}", e.getMessage(), e);
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("SecurityContext: {}", SecurityContextHolder.getContext().getAuthentication());
        log.info("=== JWT Authorization Filter END ===");
        chain.doFilter(request, response);
    }

}
