package com.ues.edu.sv.rpups_ues.auth.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import com.ues.edu.sv.rpups_ues.auth.mixin.SimpleGrantedAuthorityMixin;
import com.ues.edu.sv.rpups_ues.auth.models.AuthUser;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Service
public class JWTServiceImpl implements JWTService {

    public final static String SECRET_KEY = "org.springframework.boot.devtools.restart.classloader.RestartClassLoader";
    public final static Long EXPIRATION_DATE = 1000 * 60 * 60L;
    public final static String TOKEN_PREFIX = "Bearer ";
    public final static String HEADER_STRING = "authorization";

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String create(Authentication auth) throws IOException, NoSuchAlgorithmException {

        AuthUser user = (AuthUser) auth.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        Claims claims = Jwts.claims();
        claims.put("authorities", new ObjectMapper().writeValueAsString(authorities));
        return Jwts.builder().setClaims(claims).setId(user.getId().toString())
                .setSubject(user.getUsername()).signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .compact();

    }

    @Override
    public boolean validate(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info(e.getMessage());
            return false;
        }

    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(resolve(token))
                .getBody();

    }

    @Override
    public String refreshToken(AuthUser authUser) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();

        Claims claims = Jwts.claims();
        claims.put("authorities", new ObjectMapper().writeValueAsString(authorities));
        return Jwts.builder().setClaims(claims).setId(authUser.getId().toString())
                .setSubject(authUser.getUsername()).signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .compact();
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Long getId(String token) {
        return Long.parseLong(getClaims(token).getId());
    }

    @Override
    public Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    @Override
    public String resolve(String token) {
        if (token == null || !token.startsWith(TOKEN_PREFIX))
            return null;
        return token.replace(TOKEN_PREFIX, "");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(String token) throws IOException {
        Object roles = getClaims(token).get("authorities");
        return Arrays.asList(new ObjectMapper()
                .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
                .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
    }

    @Override
    public Boolean requiresAuthentication(String header) {
        if (header == null || !header.startsWith(JWTServiceImpl.TOKEN_PREFIX))
            return false;
        return true;
    }
}
