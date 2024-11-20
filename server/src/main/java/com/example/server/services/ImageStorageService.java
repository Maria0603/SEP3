package com.example.server.services;

import com.example.server.DataServerStub;
import com.example.shared.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service public class ImageStorageService
{

  private static final String BASE_DIRECTORY = "server/images/";


  public String saveImage(byte[] imageData, Category category, String imageName)
      throws IOException
  {
    // Get the sanitized subdirectory name from the category enum
    String subDirectory = category.getDirectoryName();

    // Create the full directory path
    File categoryDir = new File(BASE_DIRECTORY + subDirectory);
    if (!categoryDir.exists() && !categoryDir.mkdirs())
    {
      throw new IOException(
          "Failed to create directory: " + categoryDir.getAbsolutePath());
    }

    // Generate a unique file name to avoid collisions
    String uniqueImageName = UUID.randomUUID() + "_" + imageName;

    // Define the full file path
    String filePath =
        categoryDir.getPath() + File.separator + uniqueImageName;

    // Write the image data to the file
    Files.write(new File(filePath).toPath(), imageData);
/*
    try
    {
      ByteArrayInputStream inStreamObj = new ByteArrayInputStream(imageData);
      BufferedImage newImage = ImageIO.read(inStreamObj);

      ImageIO.write(newImage, "jpg", new File(filePath));
    }
    catch (IOException e)
    {
      e.printStackTrace();

    }*/

    return filePath; // Return the saved file path
  }


  public String getCategoryDirectory(Category category)
  {
    return BASE_DIRECTORY + category.getDirectoryName();
  }
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
  }*/

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
  }

}
