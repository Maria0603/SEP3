package com.example.server.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsResponseDto
{
    private String token;
    private String refreshToken;
    private Date expiresAt;

    private String id;
    private String role;
}
