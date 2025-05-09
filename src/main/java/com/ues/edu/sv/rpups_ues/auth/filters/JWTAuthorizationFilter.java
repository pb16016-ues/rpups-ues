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

        String header = request.getHeader(JWTServiceImpl.HEADER_STRING);

        if (!jwtService.requiresAuthentication(header)) {
            chain.doFilter(request, response);
            return;
        }
        log.info(request.getRequestURI() + "    " + header);
        log.info(request.getRequestURI() + "    " + jwtService.getAuthorities(header).toString());

        UsernamePasswordAuthenticationToken authenticationToken = null;
        if (jwtService.validate(header)) {
            authenticationToken = new UsernamePasswordAuthenticationToken(jwtService.getId(header), null,
                    jwtService.getAuthorities(header));
        }

        log.info(jwtService.getAuthorities(header).toString());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

}
