package com.pinapp.clientservice.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        String secret = "my-super-secret-key-that-is-long-enough-123456";
        ReflectionTestUtils.setField(jwtUtil, "secretKey", "my-super-secret-key-that-is-long-enough-123456");
    }

    @Test
    void shouldGenerateAndExtractTokenCorrectly() {
        // Arrange
        String username = "admin";
        String role = "ADMIN";

        // Act
        String token = jwtUtil.generateToken(username, role);

        // Assert
        Assertions.assertNotNull(token);
        Assertions.assertEquals(username, jwtUtil.extractUsername(token));
        Assertions.assertEquals(role, jwtUtil.extractRole(token));
    }

    @Test
    void shouldExtractClaimsCorrectly() {
        // Arrange
        String token = jwtUtil.generateToken("user", "USER");

        // Act
        Claims claims = jwtUtil.extractClaims(token);

        // Assert
        Assertions.assertEquals("user", claims.getSubject());
        Assertions.assertEquals("USER", claims.get("role", String.class));
        Assertions.assertNotNull(claims.getExpiration());
        Assertions.assertNotNull(claims.getIssuedAt());
    }
}
