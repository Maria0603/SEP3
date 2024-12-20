package com.example.server.dto.auth;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponseDto implements UserDetails
{

  private String email;
  private String id;
  private String password;
  private String role;
  public UserDetailsResponseDto(String email, String password, String role)
  {
    this.email = email;
    this.password = password;
    this.role = role;
  }

  @Override public Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities()
  {
    return Collections.singleton(() -> role); // Returns a single role authority
  }

  @Override public String getPassword()
  {
    return password;
  }

  @Override public String getUsername()
  {
    return email;
  }

  @Override public boolean isAccountNonExpired()
  {
    return true;
  }

  @Override public boolean isAccountNonLocked()
  {
    return true;
  }

  @Override public boolean isCredentialsNonExpired()
  {
    return true;
  }

  @Override public boolean isEnabled()
  {
    return true;
  }
}
