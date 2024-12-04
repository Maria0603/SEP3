package com.example.server.auth;

import com.example.server.services.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableWebSecurity @EnableMethodSecurity public class SecurityConfig
{

  @Autowired private UserDetailsService userDetailsService;
  @Autowired private JWTAuthFilter jwtAuthFIlter;

  @Bean public SecurityFilterChain securityFilterChain(
      HttpSecurity httpSecurity) throws Exception
  {
    httpSecurity.csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults()).authorizeHttpRequests(
            request -> request.requestMatchers(
                    "/auth/**" /*, "/public/**"*/) // Do we have other public features?
                .permitAll().requestMatchers("/business/**")
                .hasAnyAuthority("BUSINESS", "ADMIN") // Business + Admin access
                .requestMatchers("/customer/**")
                .hasAnyAuthority("CUSTOMER", "ADMIN") // Customer + Admin access
                .requestMatchers("/admin/**")
                .hasAuthority("ADMIN") // Strictly Admin access
                .anyRequest().authenticated()).sessionManagement(
            manager -> manager.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthFIlter,
            UsernamePasswordAuthenticationFilter.class);
    return httpSecurity.build();
  }

  @Bean public AuthenticationProvider authenticationProvider()
  {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProvider;
  }

  @Bean public PasswordEncoder passwordEncoder()
  {
    return new BCryptPasswordEncoder();
  }

  @Bean public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception
  {
    return authenticationConfiguration.getAuthenticationManager();
  }

}