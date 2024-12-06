package com.example.shared.converters;

import com.example.shared.model.Address;

public class AddressConverter
{
  public static com.example.sep3.grpc.Address convertAddressToGrpcAddress(
      Address address)
  {
    return com.example.sep3.grpc.Address.newBuilder().setCity(address.getCity())
        .setCountry(address.getCountry()).setNumber(address.getNumber())
        .setPostalCode(address.getPostalCode()).setState(address.getState())
        .setCounty(address.getCounty()).setStreet(address.getStreet()).build();

  }

  public static Address convertGrpcAddressToAddress(
      com.example.sep3.grpc.Address addressGrpc)
  {
    Address address = new Address();
    address.setCity(addressGrpc.getCity());
    address.setCountry(addressGrpc.getCountry());
    address.setNumber(addressGrpc.getNumber());
    address.setPostalCode(addressGrpc.getPostalCode());
    address.setState(addressGrpc.getState());
    address.setCounty(addressGrpc.getCounty());
    address.setStreet(addressGrpc.getStreet());
    return address;
  }


}
