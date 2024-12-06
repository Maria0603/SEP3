package com.example.server.security;

import com.example.sep3.grpc.UserByEmailRequest;
import com.example.sep3.grpc.UserResponse;
import com.example.server.DataServerStub;
import com.example.server.dto.auth.UserDetailsResponseDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service public class UserDetailsService
    implements org.springframework.security.core.userdetails.UserDetailsService
{

  private final DataServerStub dataServerStub;

  public UserDetailsService(DataServerStub dataServerStub)
  {
    this.dataServerStub = dataServerStub;
  }

  @Override public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException
  {
    //Extract the user from database
    try
    {
      UserResponse userResponse = dataServerStub.getUserByEmail(
          UserByEmailRequest.newBuilder().setEmail(username).build());

      //Convert to dao, because dao implements UserDetails interface
      return new UserDetailsResponseDto(userResponse.getEmail(),
          userResponse.getHashedPassword(), userResponse.getRole());
    }
    catch (Exception businessException)
    {
      throw new UsernameNotFoundException(
          "Account not found for email: " + username);
    }
  }
}
