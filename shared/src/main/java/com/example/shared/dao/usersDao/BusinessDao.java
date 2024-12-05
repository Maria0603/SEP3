package com.example.shared.dao.usersDao;

import com.example.shared.dao.auxDao.AddressDao;
import com.mongodb.client.model.geojson.Point;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Document(collection = "businesses") public class BusinessDao
    implements UserDetails
{
  @Id private String id;
  @Field("email") @Indexed(unique = true) private String email;
  @Field("phone_number") private String phoneNumber;
  @Field("hashed_password") private String password;
  @Field("role") private String role; // "CUSTOMER" or "BUSINESS"
  @Field("business_name") private String businessName;
  @Field("cvr") private String cvr;
  @Field("logo_path") private String logoPath;
  @Field("address") private AddressDao address;
  @Field("latitude") private double latitude;
  @Field("longitude") private double longitude;

  @Field("location")
  @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
  private GeoJsonPoint location;


  public GeoJsonPoint getLocation()
  {
    return location;
  }

  public void setLocation(GeoJsonPoint location)
  {
    this.location = location;
  }

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


  public double getLongitude()
  {
    return longitude;
  }

  public void setLongitude(double longitude)
  {
    this.longitude = longitude;
  }

  public double getLatitude()
  {
    return latitude;
  }

  public void setLatitude(double latitude)
  {
    this.latitude = latitude;
  }

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

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
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
