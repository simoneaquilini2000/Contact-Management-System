package com.app.contactmanagementsystem.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.app.contactmanagementsystem.exceptions.AuthenticationNotFoundException;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUtilsTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Jwt jwt;

    private AuthUtils authUtils;

    @BeforeEach
    void setUp() {
        authUtils = new AuthUtils();
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentJwt_shouldReturnJwt_whenAuthenticationIsJwtAuthenticationToken() {
        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt);
        when(securityContext.getAuthentication()).thenReturn(jwtAuth);

        Jwt result = authUtils.getCurrentJwt();

        assertThat(result).isEqualTo(jwt);
    }

    @Test
    void getCurrentJwt_shouldThrowException_whenAuthenticationIsNotJwtAuthenticationToken() {
        Authentication otherAuth = new UsernamePasswordAuthenticationToken("user", "password");
        when(securityContext.getAuthentication()).thenReturn(otherAuth);

        assertThatThrownBy(() -> authUtils.getCurrentJwt())
                .isInstanceOf(AuthenticationNotFoundException.class)
                .hasMessage("No JWT authentication found in security context");
    }

    @Test
    void getCurrentJwt_shouldThrowException_whenAuthenticationIsNull() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThatThrownBy(() -> authUtils.getCurrentJwt())
                .isInstanceOf(AuthenticationNotFoundException.class)
                .hasMessage("No JWT authentication found in security context");
    }

    @Test
    void getCurrentUserId_shouldReturnSubject_whenJwtIsPresent() {
        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt);
        when(securityContext.getAuthentication()).thenReturn(jwtAuth);
        when(jwt.getSubject()).thenReturn("user-123");

        String result = authUtils.getCurrentUserId();

        assertThat(result).isEqualTo("user-123");
    }

    @Test
    void getCurrentUserId_shouldThrowException_whenNoJwtAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThatThrownBy(() -> authUtils.getCurrentUserId())
                .isInstanceOf(AuthenticationNotFoundException.class);
    }

    @Test
    void getClaim_shouldReturnClaimValue_whenClaimExists() {
        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt);
        when(securityContext.getAuthentication()).thenReturn(jwtAuth);
        when(jwt.getClaimAsString("email")).thenReturn("jane.doe@example.com");

        String result = authUtils.getClaim("email");

        assertThat(result).isEqualTo("jane.doe@example.com");
    }

    @Test
    void getClaim_shouldReturnNull_whenClaimDoesNotExist() {
        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt);
        when(securityContext.getAuthentication()).thenReturn(jwtAuth);
        when(jwt.getClaimAsString("nonexistent")).thenReturn(null);

        String result = authUtils.getClaim("nonexistent");

        assertThat(result).isNull();
    }

    @Test
    void getClaim_shouldThrowException_whenNoJwtAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThatThrownBy(() -> authUtils.getClaim("email"))
                .isInstanceOf(AuthenticationNotFoundException.class);
    }

    @Test
    void getCurrentJwt_shouldUseActualJwtInstance_withRealClaims() {
        Jwt realJwt = Jwt.withTokenValue("token-value")
                .header("alg", "RS256")
                .claim("sub", "user-456")
                .claim("name", "Jane Doe")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(realJwt);
        when(securityContext.getAuthentication()).thenReturn(jwtAuth);

        String userId = authUtils.getCurrentUserId();
        String name = authUtils.getClaim("name");

        assertThat(userId).isEqualTo("user-456");
        assertThat(name).isEqualTo("Jane Doe");
    }
}
