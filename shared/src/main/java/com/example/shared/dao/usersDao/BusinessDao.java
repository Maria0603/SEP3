package com.example.shared.dao.usersDao;

import com.example.shared.dao.auxDao.AddressDao;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "businesses") public class BusinessDao extends UserDao
{
  @Field("business_name") private String businessName;
  @Field("cvr") private String cvr;
  @Field("logo_path") private String logoPath;
  @Field("address") private AddressDao address;

  public AddressDao getAddress()
  {
    return address;
  }

  public void setAddress(AddressDao address)
  {
    this.address = address;
  }

  public String getLogoPath()
  {
    return logoPath;
  }

  public void setLogoPath(String logoPath)
  {
    this.logoPath = logoPath;
  }

  public String getCvr()
  {
    return cvr;
  }

  public void setCvr(String cvr)
  {
    this.cvr = cvr;
  }

  public String getBusinessName()
  {
    return businessName;
  }

  public void setBusinessName(String businessName)
  {
    this.businessName = businessName;
  }

}
