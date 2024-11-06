package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "offer")
public class Offer
{
  @Id
  private String id;
  private String title, description;
  private String[] categories;
  private int price, numberOfFoodBags;
  private LocalDate pickupDate;
  private LocalTime pickupTimeStart;
  private LocalTime pickupTimeEnd;


  public Offer(String id, String title, String description, int price,
      LocalDate pickupDate, LocalTime pickupTimeStart, LocalTime pickupTimeEnd,
      String[] categories, int numberOfFoodBags)
  {
    this.id = id;
    this.title = title;
    this.description = description;
    this.price = price;
    this.pickupDate = pickupDate;
    this.pickupTimeStart = pickupTimeStart;
    this.pickupTimeEnd = pickupTimeEnd;
    this.categories = categories;
    this.numberOfFoodBags=numberOfFoodBags;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
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

  public String[] getCategories()
  {
    return categories;
  }

  public void setCategories(String[] categories)
  {
    this.categories = categories;
  }

  public int getNumberOfFoodBags()
  {
    return numberOfFoodBags;
  }

  public void setNumberOfFoodBags(int numberOfFoodBags)
  {
    this.numberOfFoodBags = numberOfFoodBags;
  }
  public int getPrice()
  {
    return price;
  }

  public void setPrice(int price)
  {
    this.price = price;
  }

  public LocalTime getPickupTimeEnd()
  {
    return pickupTimeEnd;
  }

  public void setPickupTimeEnd(LocalTime pickupTimeEnd)
  {
    this.pickupTimeEnd = pickupTimeEnd;
  }

  public LocalTime getPickupTimeStart()
  {
    return pickupTimeStart;
  }

  public void setPickupTimeStart(LocalTime pickupTimeStart)
  {
    this.pickupTimeStart = pickupTimeStart;
  }

  public LocalDate getPickupDate()
  {
    return pickupDate;
  }

  public void setPickupDate(LocalDate pickupDate)
  {
    this.pickupDate = pickupDate;
  }

}
