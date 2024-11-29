package com.example.shared.dao.usersDao;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

//Just to see how it works
@Document(collection = "customers") public class CustomerDao extends UserDao
{
  @Field("first_name") private String firstName;
  @Field("last_name") private String lastName;

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
