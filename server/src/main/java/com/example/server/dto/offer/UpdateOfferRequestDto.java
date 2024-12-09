package com.example.server.dto.offer;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOfferRequestDto
{
  private String id;

  private String status;

  @NotNull(message = "Title must be between 3 and 20 characters") @Size(min = 3, max = 20, message = "Title must be between 3 and 20 characters")
  private String title;

  @NotNull(message = "Description must be between 20 and 50 characters") @Size(min = 20, max = 50, message = "Description must be between 20 and 50 characters")
  private String description;

  @NotNull(message = "Original price must be a positive integer") @Positive(message = "Original price must be a positive integer")
  private Integer originalPrice;

  @NotNull(message = "Offer price must be a positive integer and less than the original price") @Positive(message = "Offer price must be a positive integer and less than the original price")
  private Integer offerPrice;

  @AssertTrue(message = "Offer price must be a positive integer and less than the original price")
  private boolean isOfferPriceValid()
  {
    return offerPrice != null && originalPrice != null
        && offerPrice <= originalPrice;
  }

  @NotNull(message = "Number of items must be a positive integer") @Positive(message = "Number of items must be a positive integer")
  private Integer numberOfItems;

  @NotNull(message = "Number of available items must be a positive integer") @Positive(message = "Number of available items must be a positive integer")
  private int numberOfAvailableItems;

  @AssertTrue(message = "Offer price must be a positive integer and less than the original price")
  private boolean isNumberOfAvailableItemsValid()
  {
    return numberOfItems > 0 && numberOfAvailableItems <= numberOfItems;
  }


  @NotNull(message = "At least one category must be provided") @Size(min = 1, message = "At least one category must be provided")
  private List<String> categories;

  @NotNull(message = "Pickup start time must be provided")
  private LocalDateTime pickupTimeStart;

  @NotNull(message = "Pickup end time must be provided")
  private LocalDateTime pickupTimeEnd;

  @AssertTrue(message = "Pickup end time must be after start time")
  private boolean isPickupTimeValid()
  {
    return pickupTimeStart != null && pickupTimeEnd != null
        && pickupTimeEnd.isAfter(pickupTimeStart);
  }
  //  TODO: Look image client - server transmission
  /*  Getting error: Bad Request because am not sending bytes yet*/
  //  @NotNull(message = "Image must be provided")
  private byte[] image;
}
