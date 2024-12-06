package com.example.shared.converters;

import com.example.shared.model.Address;

public class AddressConverter
{
  public static com.example.sep3.grpc.Address convertAddressDaoToGrpcAddress(
      Address dao)
  {
    return com.example.sep3.grpc.Address.newBuilder().setCity(dao.getCity())
        .setCountry(dao.getCountry()).setNumber(dao.getNumber())
        .setPostalCode(dao.getPostalCode()).setState(dao.getState())
        .setCounty(dao.getCounty()).setStreet(dao.getStreet()).build();

  }

  public static Address convertGrpcAddressToAddressDao(
      com.example.sep3.grpc.Address address)
  {
    Address addressDao = new Address();
    addressDao.setCity(address.getCity());
    addressDao.setCountry(address.getCountry());
    addressDao.setNumber(address.getNumber());
    addressDao.setPostalCode(address.getPostalCode());
    addressDao.setState(address.getState());
    addressDao.setCounty(address.getCounty());
    addressDao.setStreet(address.getStreet());
    return addressDao;
  }


}
