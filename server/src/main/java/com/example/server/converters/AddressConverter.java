package com.example.server.converters;

import com.example.server.dto.address.AddressDto;
import com.example.shared.model.Address;

public class AddressConverter
{
  public static com.example.sep3.grpc.Address convertAddressDtoToGrpcAddress(AddressDto dto)
  {
    return com.example.sep3.grpc.Address.newBuilder().setCity(dto.getCity())
        .setCountry(dto.getCountry()).setNumber(dto.getNumber())
        .setPostalCode(dto.getPostalCode()).setState(dto.getState())
        .setCounty(dto.getCounty()).setStreet(dto.getStreet()).build();

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

  public static AddressDto convertGrpcAddressToAddressDto(
      com.example.sep3.grpc.Address address)
  {
    AddressDto addressDto = new AddressDto();
    addressDto.setCity(address.getCity());
    addressDto.setCountry(address.getCountry());
    addressDto.setNumber(address.getNumber());
    addressDto.setPostalCode(address.getPostalCode());
    addressDto.setState(address.getState());
    addressDto.setCounty(address.getCounty());
    addressDto.setStreet(address.getStreet());
    return addressDto;
  }
}
