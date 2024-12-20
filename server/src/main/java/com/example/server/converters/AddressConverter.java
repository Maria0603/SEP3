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
