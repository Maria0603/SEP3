package com.example.server.converters;

import com.example.sep3.grpc.Address;
import com.example.server.dto.address.AddressDto;
import com.example.shared.dao.auxDao.AddressDao;

public class AddressConverter
{
  public static Address convertAddressDtoToGrpcAddress(AddressDto dto)
  {
    return Address.newBuilder().setCity(dto.getCity())
        .setCountry(dto.getCountry()).setNumber(dto.getNumber())
        .setPostalCode(dto.getPostalCode()).setState(dto.getState())
        .setCounty(dto.getCounty()).setStreet(dto.getStreet()).build();

  }

  public static AddressDto convertGrpcAddressToAddressDto(Address address)
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
