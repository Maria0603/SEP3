package com.example.server;

import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.dto.DateDto;
import com.example.server.dto.TimeDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OfferTestClient
{

  public static void main(String[] args)
  {
    String url = "http://localhost:8082/offers";

    // Create RestTemplate instance
    RestTemplate restTemplate = new RestTemplate();

    // Create headers for the request
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    CreateOfferRequestDto dto=new CreateOfferRequestDto();

    try
    {
      dto.setTitle("Titleeeeeeee");
      dto.setDescription("Desssssssssssssssssss");
      dto.setOfferPrice(10);
      dto.setNumberOfItems(5);
      dto.setOriginalPrice(20);
      dto.setImage(createImageByteArray());
      DateDto dateDto=new DateDto();
      dateDto.setYear(2024);
      dateDto.setMonth(12);
      dateDto.setDay(15);
      TimeDto start=new TimeDto();
      start.setHour(14);
      start.setMinute(30);
      TimeDto end=new TimeDto();
      end.setHour(15);
      end.setMinute(30);
      dto.setPickupDate(dateDto);
      dto.setPickupTimeStart(start);
      dto.setPickupTimeEnd(end);
      ArrayList<String> categories=new ArrayList<>();
      categories.add("Drink");
      categories.add("Other");
      dto.setCategories(categories);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

    // Create the multipart body
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("offer", dto);
    body.add("file", new ByteArrayResource(dto.getImage())
    {
      @Override public String getFilename()
      {
        return "test-image.jpg";
      }
    });

    // Wrap the body and headers in an HttpEntity
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
        body, headers);

    // Make the POST request
    ResponseEntity<String> response = restTemplate.postForEntity(url,
        requestEntity, String.class);

    // Print the response
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response body: " + response.getBody());
  }
  //Method to create a dummy red image, for testing purposes; do not delete
  public static byte[] createImageByteArray() throws IOException
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

