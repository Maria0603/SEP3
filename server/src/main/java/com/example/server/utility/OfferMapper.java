package com.example.server.utility;

import com.example.sep3.grpc.Date;
import com.example.sep3.grpc.SaveOfferRequest;
import com.example.sep3.grpc.Time;
import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.dto.OfferDto;
import com.example.shared.dao.DateDao;
import com.example.shared.dao.OfferDao;
import com.example.shared.dao.TimeDao;
import com.google.protobuf.ByteString;

public class OfferMapper
{
//Map CreateOfferRequestDto to OfferDao
  public static OfferDao toOfferDao(CreateOfferRequestDto createOfferRequestDto)
  {
    OfferDao offerDao = new OfferDao();

    offerDao.setTitle(createOfferRequestDto.getTitle());
    offerDao.setDescription(createOfferRequestDto.getDescription());
    offerDao.setCategories(createOfferRequestDto.getCategories());
    offerDao.setPrice(createOfferRequestDto.getPrice());
    offerDao.setNumberOfFoodBags(createOfferRequestDto.getNumberOfFoodBags());

    // Convert DateDto to DateDao and TimeDto to TimeDao for saving in the DB
    offerDao.setPickupDate(
        DateConverter.toDateDao(createOfferRequestDto.getPickupDate()));
    offerDao.setPickupTimeStart(
        TimeConverter.toTimeDao(createOfferRequestDto.getPickupTimeStart()));
    offerDao.setPickupTimeEnd(
        TimeConverter.toTimeDao(createOfferRequestDto.getPickupTimeEnd()));

    offerDao.setImage(createOfferRequestDto.getImage());

    // Default status
    offerDao.setStatus("available");

    return offerDao;
  }

  // Map OfferDao to OfferDto
  public static OfferDto toOfferDto(OfferDao offerDao)
  {
    OfferDto offerDto = new OfferDto();

    offerDto.setId(offerDao.getId());
    offerDto.setTitle(offerDao.getTitle());
    offerDto.setDescription(offerDao.getDescription());
    /*offerDto.setCategories(offerDao.getCategories());
    offerDto.setPrice(offerDao.getPrice());
    offerDto.setNumberOfFoodBags(offerDao.getNumberOfFoodBags());

    // Convert DateDao to DateDto and TimeDao to TimeDto for the response
    offerDto.setPickupDate(DateConverter.toDateDto(offerDao.getPickupDate()));
    offerDto.setPickupTimeStart(
        TimeConverter.toTimeDto(offerDao.getPickupTimeStart()));
    offerDto.setPickupTimeEnd(
        TimeConverter.toTimeDto(offerDao.getPickupTimeEnd()));

    offerDto.setImage(offerDao.getImage());*/
    offerDto.setStatus(offerDao.getStatus());

    return offerDto;
  }

}
