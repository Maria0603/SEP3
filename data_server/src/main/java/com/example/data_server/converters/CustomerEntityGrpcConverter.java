package com.example.data_server.converters;

import com.example.sep3.grpc.BusinessOnMap;
import com.example.sep3.grpc.CustomerResponse;
import com.example.sep3.grpc.RegisterCustomerRequest;
import com.example.shared.entities.usersEntities.Business;
import com.example.shared.entities.usersEntities.Customer;

public class CustomerEntityGrpcConverter
{

  public static CustomerResponse buildCustomerResponse(Customer customer)
  {
    return CustomerResponse.newBuilder().setFirstName(customer.getFirstName())
        .setLastName(customer.getLastName()).setEmail(customer.getEmail())
        .setPhoneNumber(customer.getPhoneNumber())
        .setHashedPassword(customer.getPassword()).setId(customer.getId())
        .setRole(customer.getRole()).build();
  }

  public static Customer generateCustomerFromRegisterCustomerRequest(
      RegisterCustomerRequest request)
  {
    Customer customer = new Customer();
    customer.setFirstName(request.getFirstName());
    customer.setLastName(request.getLastName());
    customer.setEmail(request.getEmail());
    customer.setPassword(request.getHashedPassword());
    customer.setPhoneNumber(request.getPhoneNumber());
    customer.setRole(request.getRole());
    return customer;
  }
}
