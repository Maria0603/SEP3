package com.example.shared.dao.usersDao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users") public abstract class UserDao
{
  @Id private String id;
  @Field("email") @Indexed(unique = true) private String email;
  @Field("phone_number") private String phoneNumber;
  @Field("hashed_password") private String hashedPassword;
  @Field("role") private String role; // "CUSTOMER" or "BUSINESS"

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }

  public String getRole()
  {
    return role;
  }

  public void setRole(String role)
  {
    this.role = role;
  }

  public String getHashedPassword()
  {
    return hashedPassword;
  }

  public void setHashedPassword(String hashedPassword)
  {
    this.hashedPassword = hashedPassword;
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

}

