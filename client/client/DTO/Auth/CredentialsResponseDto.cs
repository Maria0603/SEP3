﻿namespace client.DTO.Auth;

public class CredentialsResponseDto
{
    public string Token { get; set; }
    public string RefreshToken { get; set; }
}