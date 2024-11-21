package com.example.server.services;

import com.example.shared.model.Category;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;

@Service public class ImageStorageService
{

  private static final String BASE_DIRECTORY = "server/images/";

  //When dealing with categories we will use the Category enum (in shared), but for now, we have strings
  public String saveImage(byte[] imageData, String category, String businessId)
      throws IOException
  {
    // Get the sanitized subdirectory name from the category enum
    String subDirectory = category.toLowerCase().replace(" ", "_");

    // Construct the image name as BusinessId_Category.jpg
    String imageName = businessId + "_" + subDirectory + ".jpg";

    // Create the full directory path
    File categoryDir = new File(BASE_DIRECTORY + subDirectory);
    if (!categoryDir.exists() && !categoryDir.mkdirs())
      throw new IOException(
          "Failed to create directory: " + categoryDir.getAbsolutePath());

    // Check if the file already exists before writing; if it does, use it
    File file = new File(categoryDir, imageName);
    if (!file.exists())
      // Write the image data to the file
      Files.write(file.toPath(), imageData);

    return "images/" + subDirectory + "/" + imageName;
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
