package com.app.contactmanagementsystem.utils;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    public Jwt getCurrentJwt() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken();
        }
        throw new IllegalStateException("No JWT authentication found in security context");
    }

    public String getCurrentUserId() {
        return getCurrentJwt().getSubject();
    }

    public String getClaim(String claimName) {
        return getCurrentJwt().getClaimAsString(claimName);
    }
}
