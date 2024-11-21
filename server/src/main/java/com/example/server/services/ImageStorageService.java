package com.example.server.services;

import com.example.shared.model.Category;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

@Service public class ImageStorageService
{

  private static final String BASE_DIRECTORY = "server/images/";

  public ImageStorageService()
  {
    File file = new File(BASE_DIRECTORY);
    if (!file.exists())
      file.mkdirs();

  }

  public String saveImage(byte[] imageData) throws IOException
  {

    // Generates a random unique ID
    String imageName = UUID.randomUUID() + ".jpg";

    File file = new File(BASE_DIRECTORY, imageName);
    Files.write(file.toPath(), imageData);

    return "images/" + imageName;
  }

  public void deleteImage(String imagePath)
  {
    File file = new File(imagePath);
    if (file.exists())
    {
      boolean deleted = file.delete();
      if (!deleted)
        throw new IllegalArgumentException(
            "Failed to delete the image: " + imagePath);
    }
    else
      throw new IllegalArgumentException("Image not found: " + imagePath);
  }

  //Good methods, do not delete
  /*
  private String saveImage(byte[] imageBytes, String offerId)
  {
    String pathToImage = null;
    try
    {
      ByteArrayInputStream inStreamObj = new ByteArrayInputStream(imageBytes);
      BufferedImage newImage = ImageIO.read(inStreamObj);

      pathToImage = uploadDirectory + offerId + ".jpg";
      ImageIO.write(newImage, "jpg", new File(pathToImage));
    }
    catch (IOException e)
    {
      e.printStackTrace();

    }
    return pathToImage;
  }

  //Method to create a dummy red image, for testing purposes; do not delete
  private byte[] createImageByteArray() throws IOException
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

  private byte[] extractImage(String imagePath)
  {
    try
    {
      BufferedImage image = ImageIO.read(new File(imagePath));
      ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
      ImageIO.write(image, "jpg", outStreamObj);
      return outStreamObj.toByteArray();
    }
    catch (IOException e)
    {
      e.printStackTrace();

    }
    return null;
  }*/

}
