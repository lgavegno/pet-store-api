package com.mycompany.petstore.service;

import com.mycompany.petstore.model.Role;
import com.mycompany.petstore.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtService jwtService;

    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long EXPIRATION_TIME_MS = 3600000L; // 1 hour
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final Role TEST_ROLE = Role.USER;

    private User testUser;
    private Key signingKey;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = User.builder()
                .id(TEST_USER_ID)
                .username(TEST_USERNAME)
                .email(TEST_EMAIL)
                .password("encodedPassword")
                .role(TEST_ROLE)
                .build();

        // Setup JWT properties
        when(jwtProperties.getSecretKey()).thenReturn(SECRET_KEY);
        when(jwtProperties.getExpirationTime()).thenReturn(EXPIRATION_TIME_MS);

        // Initialize the signing key
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        signingKey = Keys.hmacShaKeyFor(keyBytes);

        // Initialize the JwtService with the mocked properties
        jwtService = new JwtService(jwtProperties);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        // When
        String token = jwtService.generateToken(testUser);

        // Then
        assertThat(token).isNotBlank();
        
        // Verify the token can be parsed and contains the expected claims
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo(TEST_USERNAME);
        assertThat(claims.get("id", Long.class)).isEqualTo(TEST_USER_ID);
        assertThat(claims.get("role", String.class)).isEqualTo(TEST_ROLE.name());
        
        // Verify expiration is set correctly (within 1 second of expected)
        Date expiration = claims.getExpiration();
        long expirationTime = expiration.getTime();
        long expectedExpirationTime = System.currentTimeMillis() + EXPIRATION_TIME_MS;
        assertThat(expirationTime).isCloseTo(expectedExpirationTime, within(1000L));
    }

    @Test
    void generateToken_WithExtraClaims_ShouldIncludeAllClaims() {
        // Given
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customClaim", "customValue");

        // When
        String token = jwtService.generateToken(extraClaims, testUser);

        // Then
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.get("customClaim", String.class)).isEqualTo("customValue");
    }

    @Test
    void isTokenValid_WithValidToken_ShouldReturnTrue() {
        // Given
        String token = Jwts.builder()
                .setSubject(TEST_USERNAME)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        // When
        boolean isValid = jwtService.isTokenValid(token, testUser);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    void isTokenValid_WithExpiredToken_ShouldReturnFalse() {
        // Given - Token expired 1 hour ago
        String expiredToken = Jwts.builder()
                .setSubject(TEST_USERNAME)
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000)) // 2 hours ago
                .setExpiration(new Date(System.currentTimeMillis() - 3600000)) // 1 hour ago
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        // When
        boolean isValid = jwtService.isTokenValid(expiredToken, testUser);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void isTokenValid_WithInvalidUser_ShouldReturnFalse() {
        // Given
        String token = Jwts.builder()
                .setSubject("differentUser")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        // When
        boolean isValid = jwtService.isTokenValid(token, testUser);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void extractUsername_WithValidToken_ShouldReturnUsername() {
        // Given
        String token = Jwts.builder()
                .setSubject(TEST_USERNAME)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        // When
        String username = jwtService.extractUsername(token);

        // Then
        assertThat(username).isEqualTo(TEST_USERNAME);
    }

    @Test
    void extractClaim_WithValidToken_ShouldReturnClaimValue() {
        // Given
        String claimName = "customClaim";
        String claimValue = "testValue";
        
        String token = Jwts.builder()
                .setSubject(TEST_USERNAME)
                .claim(claimName, claimValue)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        // When
        String extractedValue = jwtService.extractClaim(token, claims -> claims.get(claimName, String.class));

        // Then
        assertThat(extractedValue).isEqualTo(claimValue);
    }
}
