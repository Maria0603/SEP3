package com.example.server.services.security;

import com.example.sep3.grpc.BusinessByEmailRequest;
import com.example.sep3.grpc.BusinessResponse;
import com.example.server.DataServerStub;
import com.example.server.converters.AddressConverter;
import com.example.shared.dao.usersDao.BusinessDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService
{

  private final DataServerStub dataServerStub;

  public UserDetailsService(DataServerStub dataServerStub)
  {
    this.dataServerStub = dataServerStub;
  }

  @Override public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException
  {
    //Extract the business from database
    BusinessResponse businessResponse = dataServerStub.getBusinessByEmail(
        BusinessByEmailRequest.newBuilder().setEmail(username).build());

    //Convert to dao, because dao implements UserDetails interface
    return generateBusinessDaoFromBusinessResponse(
        businessResponse);
  }

  private BusinessDao generateBusinessDaoFromBusinessResponse(
      BusinessResponse response)
  {
    BusinessDao dao = new BusinessDao();
    dao.setId(response.getId());
    dao.setCvr(response.getCvr());
    dao.setEmail(response.getEmail());
    dao.setAddress(
        AddressConverter.convertGrpcAddressToAddressDao(response.getAddress()));
    dao.setBusinessName(response.getBusinessName());
    dao.setPhoneNumber(response.getPhoneNumber());
    dao.setRole(response.getRole());
    dao.setLogoPath(response.getLogoPath());

    dao.setPassword(response.getHashedPassword());
    return dao;
  }
}
