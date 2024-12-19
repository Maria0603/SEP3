package com.example.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component public class JWTUtils
{

  private SecretKey Key;
  private static final long EXPIRATION_TIME = 864000000L; //24hours = 86400000 milliseconds, so 10 days

  public JWTUtils()
  {
    String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";

    byte[] keyBytes = Base64.getDecoder()
        .decode(secreteString.getBytes(StandardCharsets.UTF_8));

    this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
  }

  public static long getExpirationTime()
  {
    return EXPIRATION_TIME;
  }

  public String generateToken(UserDetails userDetails, String userId)
  {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList())); // Add roles as claims
    claims.put("userId", userId);
    return Jwts.builder().subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(Key).claims(claims).compact();
  }

  public String generateRefreshToken(HashMap<String, Object> claims,
      UserDetails userDetails)
  {
    return Jwts.builder().claims(claims).subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(Key).compact();
  }

  public String extractUsername(String token)
  {
    return extractClaims(token, Claims::getSubject);
  }

  public List<String> extractRoles(String token)
  {
    return extractClaims(token, claims -> claims.get("role", List.class));
  }

  private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction)
  {
    return claimsTFunction.apply(
        Jwts.parser().verifyWith(Key).build().parseSignedClaims(token)
            .getPayload());
  }

  public boolean isTokenValid(String token, UserDetails userDetails)
  {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(
        token));
  }

  public boolean isTokenExpired(String token)
  {
    return extractClaims(token, Claims::getExpiration).before(new Date());
  }

  public String extractUserId(String token)
  {
    return extractClaims(token, claims -> claims.get("userId", String.class));
  }

}
