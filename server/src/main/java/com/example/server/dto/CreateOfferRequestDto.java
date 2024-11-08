package com.example.server.dto;

import com.example.sep3.grpc.Date;
import com.example.sep3.grpc.Time;
import com.example.shared.dao.DateDao;
import com.example.shared.dao.TimeDao;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

public class CreateOfferRequestDto
{
    private String title, description;
    private ArrayList<String> categories;
    private int price, numberOfFoodBags;

    private DateDto pickupDate;
    private TimeDto pickupTimeStart;
    private TimeDto pickupTimeEnd;

    private byte[] image;

    public byte[] getImage()
    {
      return image;
    }

    public void setImage(byte[] image)
    {
      this.image = image;
    }

    public String getTitle()
    {
      return title;
    }

    public void setTitle(String title)
    {
      this.title = title;
    }
    public String getDescription()
    {
      return description;
    }

    public void setDescription(String description)
    {
      this.description = description;
    }

    public ArrayList<String> getCategories()
    {
      return categories;
    }


    public void setCategories(ArrayList<String> categories)
    {
      this.categories = categories;
    }

    public int getPrice()
    {
      return price;
    }

    public void setPrice(int price)
    {
      this.price = price;
    }

    public int getNumberOfFoodBags()
    {
      return numberOfFoodBags;
    }

    public void setNumberOfFoodBags(int numberOfFoodBags)
    {
      this.numberOfFoodBags = numberOfFoodBags;
    }

    public TimeDto getPickupTimeEnd()
    {
      return pickupTimeEnd;
    }

    public void setPickupTimeEnd(TimeDto pickupTimeEnd)
    {
      this.pickupTimeEnd = pickupTimeEnd;
    }

    public TimeDto getPickupTimeStart()
    {
      return pickupTimeStart;
    }

    public void setPickupTimeStart(TimeDto pickupTimeStart)
    {
      this.pickupTimeStart = pickupTimeStart;
    }

    public DateDto getPickupDate()
    {
      return pickupDate;
    }

    public void setPickupDate(DateDto pickupDate)
    {
      this.pickupDate = pickupDate;
    }


}
