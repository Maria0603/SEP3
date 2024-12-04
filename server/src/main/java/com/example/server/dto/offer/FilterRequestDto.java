package com.example.server.dto.offer;

import com.example.server.dto.date_time.TimeDto;
import java.util.List;

public class FilterRequestDto {
  private List<String> categories;
  private Double minOfferPrice;
  private Double maxOfferPrice;
  private TimeDto pickupTimeStart;
  private TimeDto pickupTimeEnd;

  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public Double getMinPrice() {
    return minOfferPrice;
  }

  public void setMinPrice(Double minPrice) {
    this.minOfferPrice = minPrice;
  }

  public Double getMaxPrice() {
    return maxOfferPrice;
  }

  public void setMaxPrice(Double maxPrice) {
    this.maxOfferPrice = maxPrice;
  }

  public TimeDto getStartTime() {
    return getStartTime();
  }

  public void setStartTime(TimeDto startTime) {
    this.pickupTimeStart = startTime;
  }

  public TimeDto getEndTime() {
    return pickupTimeEnd;
  }

  public void setEndTime(TimeDto endTime) {
    this.pickupTimeEnd = endTime;
  }
}
