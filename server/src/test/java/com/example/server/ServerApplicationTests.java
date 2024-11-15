package com.example.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@SpringBootTest class ServerApplicationTests
{

  @Test void contextLoads()
  {
    /*
    DataServerStub dataServerStub = context.getBean(DataServerStub.class);
    OfferList offerList = dataServerStub.getAvailableOffers(
        EmptyMessage.newBuilder().build());
    System.out.println("Offers:  " + offerList);

    ArrayList<String> categories = new ArrayList<>();
    categories.add("Food");
    categories.add("Vegan");
    Date pickupDate = Date.newBuilder().setDay(3).setMonth(6).setYear(2020)
        .build();
    Time pickupTimeStart = Time.newBuilder().setHour(10).setMinute(30).build();
    Time pickupTimeEnd = Time.newBuilder().setHour(12).setMinute(30).build();
    byte[] imageBytes = null;
    try
    {
      imageBytes = createImageByteArray();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    SaveOfferRequest request = SaveOfferRequest.newBuilder()
        .setTitle("Titleeeee").setDescription("Description").setOfferPrice(20)
        .setOriginalPrice(300).setNumberOfItems(3).addAllCategories(categories)
        .setPickupDate(pickupDate).setPickupTimeStart(pickupTimeStart)
        .setPickupTimeEnd(pickupTimeEnd)
        .setImage(ByteString.copyFrom(imageBytes)).build();

    SaveOfferResponse saveResponse = dataServerStub.saveOffer(request);

    System.out.println("Saved offer id:   " + saveResponse.getId());
     */
  }
  //Method to create a dummy red image, for testing purposes; do not delete
  private static byte[] createImageByteArray() throws IOException
  {
    // Create a 200x200 BufferedImage with RGB color
    BufferedImage bufferedImage = new BufferedImage(200, 200,
        BufferedImage.TYPE_INT_RGB);

    // Fill the image with a solid color
    for (int y = 0; y < 200; y++)
      for (int x = 0; x < 200; x++)
        bufferedImage.setRGB(x, y, (255 << 16) | (0 << 8) | 0); // Red

    // Convert BufferedImage to byte array in JPEG format
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(bufferedImage, "jpg", baos);
    return baos.toByteArray();
  }

}
