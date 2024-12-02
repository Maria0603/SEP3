package com.example.shared.dao.usersDao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//Just to see how it works
@Document(collection = "customers") public class CustomerDao implements
    UserDetails
{
  @Id private String id;
  @Field("email") @Indexed(unique = true) private String email;
  @Field("phone_number") private String phoneNumber;
  @Field("hashed_password") private String password;
  @Field("role") private String role; // "CUSTOMER" or "BUSINESS"

  @Field("first_name") private String firstName;
  @Field("last_name") private String lastName;

  // the email will be used to log in, not the username, but the method must be called getUsername
  @Override public String getUsername()
  {
    return email;
  }

  @Override public Collection<? extends GrantedAuthority> getAuthorities()
  {
    return List.of(new SimpleGrantedAuthority(role));
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

  public String getRole()
  {
    return role;
  }

  public void setRole(String role)
  {
    this.role = role;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }


  public String getLastName()
  {
    return lastName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

}
