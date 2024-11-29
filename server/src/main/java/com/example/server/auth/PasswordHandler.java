package com.example.server.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHandler
{
  private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public static String hashPassword(String rawPassword)
  {
    return encoder.encode(rawPassword);
  }

  public static boolean matches(String rawPassword, String hashedPassword)
  {
    return encoder.matches(rawPassword, hashedPassword);
  }
}

