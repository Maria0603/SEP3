package com.example.data_server.utility;

import com.example.sep3.grpc.Address;
import com.example.shared.dao.AddressDao;

public class AddressConverter
{
  public static Address convertAddressDaoToGrpcAddress(AddressDao dao)
  {
    return Address.newBuilder().setCity(dao.getCity())
        .setCountry(dao.getCountry()).setNumber(dao.getNumber())
        .setPostalCode(dao.getPostalCode()).setState(dao.getState())
        .setCounty(dao.getCounty()).setStreet(dao.getStreet()).build();

  }

  public static AddressDao convertGrpcAddressToAddressDao(Address address)
  {
    AddressDao addressDao = new AddressDao();
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
