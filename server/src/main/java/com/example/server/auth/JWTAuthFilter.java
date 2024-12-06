package com.example.server.auth;

import com.example.server.services.security.JWTUtils;
import com.example.server.services.security.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component public class JWTAuthFilter extends OncePerRequestFilter
{

  @Autowired private JWTUtils jwtUtils;
  @Autowired private UserDetailsService userDetailsService;

  @Override protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException
  {
    //This must be in the client's header request: Authorization: Bearer <access_token>
    final String authHeader = request.getHeader("Authorization");
    final String jwtToken;
    final String userEmail;
    final String userId;
    if (authHeader == null || authHeader.isBlank())
    {
      filterChain.doFilter(request, response);
      return;
    }
    jwtToken = authHeader.substring(7);
    userEmail = jwtUtils.extractUsername(jwtToken);
    userId = jwtUtils.extractUserId(jwtToken);
    System.out.println("Generating JWT for....: " + userEmail);
    request.setAttribute("email", userEmail);
    request.setAttribute("userId", userId);

    if (userEmail != null
        && SecurityContextHolder.getContext().getAuthentication() == null)
    {
      UserDetails userDetails = userDetailsService.loadUserByUsername(
          userEmail);

      if (jwtUtils.isTokenValid(jwtToken, userDetails))
      {
        //SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        token.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));
        //securityContext.setAuthentication(token);
        //SecurityContextHolder.setContext(securityContext);
        SecurityContextHolder.getContext().setAuthentication(token);

      }
    }
    filterChain.doFilter(request, response);
  }
}
