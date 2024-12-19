package com.example.server.contollers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.server.security.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class JWTUtilsTest {

    private JWTUtils jwtUtils;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils = new JWTUtils();
    }

    @Test
    void generateToken_Test() {
        // Arrange: Mock UserDetails object
        String username = "dummyemail221246167@example.com";
        String userId = "6754c863d5efb42169fdbee9";
        ////////////////////////////
        // String role = "CUSTOMER";
        ////////////////////////////
        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);


        // Act: Generate the token
        String token = jwtUtils.generateToken(userDetails, userId);

        // Assert: Verify that the token contains the expected username and role
        assertNotNull(token); // Check if token is not null
        assertTrue(token.startsWith("eyJhbGciOiJIUzI1NiJ9.")); // Check the token prefix (JWT starts with header part)

        // Decode the token to check claims (for simplicity, here we check for user details)
        String extractedUsername = jwtUtils.extractUsername(token);
        List<String> extractedRoles = jwtUtils.extractRoles(token);

        assertEquals(username, extractedUsername); // Check if the username matches
        ////////////////////////////////////////////
        //assertTrue(extractedRoles.contains(role)); // Check if the role is included in the toke
        ////////////////////////////////////////////

        // Also verify that the expiration time is correct
        boolean isExpired = jwtUtils.isTokenExpired(token);
        assertFalse(isExpired); // The token should not be expired if generated recently
    }
}
