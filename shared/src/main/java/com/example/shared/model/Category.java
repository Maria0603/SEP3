package com.example.shared.model;

public enum Category
{
  BREAD("Bread"), CAKE("Cake"), VEGAN("Vegan"), VEGETARIAN(
    "Vegetarian"), MEAL("Meal"), GROCERIES("Groceries"), OTHER("Other");

  private final String categoryName;

  Category(String categoryName)
  {
    this.categoryName = categoryName;
  }

  public String getCategoryName()
  {
    return categoryName;
  }
  public String getDirectoryName() {
    return categoryName.toLowerCase().replace(" ", "_");
  }

  @Override public String toString()
  {
    return categoryName;
  }
}
