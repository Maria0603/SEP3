package com.example.server.dto;

import com.example.server.converters.TimeConverter;
import jakarta.validation.constraints.*;
import java.util.List;

public class CreateOfferRequestDto
{
  @NotNull(message = "Title must be between 3 and 20 characters") @Size(min = 3, max = 20, message = "Title must be between 3 and 20 characters") private String title;

  @NotNull(message = "Description must be between 20 and 50 characters") @Size(min = 20, max = 50, message = "Description must be between 20 and 50 characters") private String description;

  @NotNull(message = "Original price must be a positive integer") @Positive(message = "Original price must be a positive integer") private Integer originalPrice;

  @NotNull(message = "Offer price must be a positive integer and less than the original price") @Positive(message = "Offer price must be a positive integer and less than the original price") private Integer offerPrice;

  @AssertTrue(message = "Offer price must be a positive integer and less than the original price") private boolean isOfferPriceValid()
  {
    return offerPrice != null && originalPrice != null
        && offerPrice < originalPrice;
  }

  @NotNull(message = "Number of items must be a positive integer") @Positive(message = "Number of items must be a positive integer") private Integer numberOfItems;

  @NotNull(message = "At least one category must be provided") @Size(min = 1, message = "At least one category must be provided") private List<String> categories;

  @NotNull(message = "Pickup date must be a valid date") private DateDto pickupDate;

  @NotNull(message = "Pickup start time must be provided") private TimeDto pickupTimeStart;

  @NotNull(message = "Pickup end time must be provided") private TimeDto pickupTimeEnd;

  @AssertTrue(message = "Pickup end time must be after start time") private boolean isPickupTimeValid()
  {
    return pickupTimeStart != null && pickupTimeEnd != null
        && (TimeConverter.convertTimeDtoToLocalTime(pickupTimeEnd)
        .isAfter(TimeConverter.convertTimeDtoToLocalTime(pickupTimeStart)));
  }

  @NotNull(message = "Image must be provided") private byte[] image;

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

  public List<String> getCategories()
  {
    return categories;
  }

  public void setCategories(List<String> categories)
  {
    this.categories = categories;
  }

  public int getOriginalPrice()
  {
    return originalPrice;
  }

  public void setOriginalPrice(int originalPrice)
  {
    this.originalPrice = originalPrice;
  }

  public int getNumberOfItems()
  {
    return numberOfItems;
  }

  public void setNumberOfItems(int numberOfItems)
  {
    this.numberOfItems = numberOfItems;
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

  public int getOfferPrice()
  {
    return offerPrice;
  }

  public void setOfferPrice(int offerPrice)
  {
    this.offerPrice = offerPrice;
  }
}
