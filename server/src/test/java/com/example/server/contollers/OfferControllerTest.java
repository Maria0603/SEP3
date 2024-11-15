package com.example.server.contollers;

import com.example.server.contollers.OfferController;
import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.dto.DateDto;
import com.example.server.dto.TimeDto;
import com.example.server.services.OfferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OfferController.class) class OfferControllerTest
{

  @Autowired private MockMvc mockMvc;

  @MockBean private OfferService offerService;

  private CreateOfferRequestDto validRequest;

  @BeforeEach void setUp()
  {
    validRequest = new CreateOfferRequestDto();
    validRequest.setTitle("Valid Title");
    validRequest.setDescription("A valid description of 25 characters");
    validRequest.setOriginalPrice(100);
    validRequest.setOfferPrice(50);
    validRequest.setNumberOfItems(10);
    validRequest.setCategories(new ArrayList<>(List.of("Category1")));

    DateDto validDate = new DateDto();
    validDate.setYear(2024);
    validDate.setMonth(11);
    validDate.setDay(20);
    validRequest.setPickupDate(validDate);

    TimeDto validTimeStart = new TimeDto();
    validTimeStart.setHour(9);
    validTimeStart.setMinute(0);
    validRequest.setPickupTimeStart(validTimeStart);

    TimeDto validTimeEnd = new TimeDto();
    validTimeEnd.setHour(17);
    validTimeEnd.setMinute(0);
    validRequest.setPickupTimeEnd(validTimeEnd);
    try
    {
      validRequest.setImage(createImageByteArray());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Test void testZeroValues() throws Exception
  {
    validRequest.setTitle("");
    performPostAndExpectError("Title must be between 3 and 20 characters");

    validRequest.setTitle("Valid Title");
    validRequest.setDescription("");
    performPostAndExpectError(
        "Description must be between 20 and 50 characters");

    validRequest.setDescription("A valid description of 25 characters");
    validRequest.setOriginalPrice(0);
    performPostAndExpectError("Original price must be a positive integer");

    validRequest.setOriginalPrice(100);
    validRequest.setOfferPrice(0);
    performPostAndExpectError(
        "Offer price must be a positive integer and less than the original price");

    validRequest.setOfferPrice(50);
    validRequest.setNumberOfItems(0);
    performPostAndExpectError("Number of items must be a positive integer");

    validRequest.setNumberOfItems(10);
    validRequest.setCategories(new ArrayList<>());
    performPostAndExpectError("At least one category must be provided");

    validRequest.setCategories(new ArrayList<>(List.of("Category1")));
    validRequest.setPickupDate(null);
    performPostAndExpectError("Pickup date must be a valid date");

    DateDto date = new DateDto();
    date.setDay(20);
    date.setMonth(11);
    date.setYear(2024);
    validRequest.setPickupDate(date);
    validRequest.setPickupTimeStart(null);
    performPostAndExpectError("Pickup start time must be provided");

    TimeDto timeStart = new TimeDto();
    timeStart.setHour(9);
    timeStart.setMinute(0);
    validRequest.setPickupTimeStart(timeStart);
    validRequest.setPickupTimeEnd(null);
    performPostAndExpectError("Pickup end time must be provided");

    TimeDto timeEnd = new TimeDto();
    timeEnd.setHour(17);
    timeEnd.setMinute(0);
    validRequest.setPickupTimeEnd(timeEnd);
    validRequest.setImage(null);
    performPostAndExpectError("Image must be provided");
  }

  @Test void testOneValidValue() throws Exception
  {
    when(offerService.saveOffer(any(CreateOfferRequestDto.class))).thenReturn(
        "12345");
    mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(validRequest))).andExpect(status().isOk())
        .andExpect(
            content().string(containsString("Offer created with ID: 12345")));
  }

  @Test void testManyValues() throws Exception
  {
    validRequest.setCategories(
        new ArrayList<>(List.of("Category1", "Category2", "Category3")));
    when(offerService.saveOffer(any(CreateOfferRequestDto.class))).thenReturn(
        "12345");
    mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(validRequest))).andExpect(status().isOk())
        .andExpect(
            content().string(containsString("Offer created with ID: 12345")));
  }

  @Test void testBoundaryValues() throws Exception
  {
    validRequest.setTitle("T");
    performPostAndExpectError("Title must be between 3 and 20 characters");

    validRequest.setTitle("ThisTitleIsExactly20");
    mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(validRequest))).andExpect(status().isOk());

    validRequest.setDescription("Short");
    performPostAndExpectError(
        "Description must be between 20 and 50 characters");

    validRequest.setDescription(
        "A valid description that is exactly 50 characters.");
    mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(validRequest))).andExpect(status().isOk());
  }

  @Test void testIllegalValues() throws Exception
  {
    validRequest.setTitle(null);
    performPostAndExpectError("Title must be between 3 and 20 characters");

    validRequest.setTitle("Valid Title");
    validRequest.setOriginalPrice(-100);
    performPostAndExpectError("Original price must be a positive integer");
  }

  @Test
  void testPickupDateTimeNotInFutureReturnsBadRequest() throws Exception {
    // Simulate the service throwing an exception for invalid pickup times
    CreateOfferRequestDto invalidRequest = new CreateOfferRequestDto();
    invalidRequest.setTitle("Valid Title");
    invalidRequest.setDescription("This is a valid description.");
    invalidRequest.setOriginalPrice(100);
    invalidRequest.setOfferPrice(50);
    invalidRequest.setNumberOfItems(10);
    invalidRequest.setCategories(new ArrayList<>(List.of("Category1")));

    try
    {
      invalidRequest.setImage(createImageByteArray());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    //Past date
    DateDto date = new DateDto();
    date.setDay(20);
    date.setMonth(1);
    date.setYear(2024);
    invalidRequest.setPickupDate(date);

    TimeDto timeStart = new TimeDto();
    timeStart.setHour(9);
    timeStart.setMinute(0);
    invalidRequest.setPickupTimeStart(timeStart);

    TimeDto timeEnd = new TimeDto();
    timeEnd.setHour(17);
    timeEnd.setMinute(0);
    invalidRequest.setPickupTimeEnd(timeEnd);

    doThrow(new IllegalArgumentException("Pickup date and time must be in the future"))
        .when(offerService).saveOffer(Mockito.any(CreateOfferRequestDto.class));

    // Perform the test
    mockMvc.perform(post("/offers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(invalidRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Pickup date and time must be in the future"));
  }

  private void performPostAndExpectError(String errorMessage) throws Exception
  {
    mockMvc.perform(post("/offers").contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(validRequest))).andExpect(status().isBadRequest())
        .andExpect(content().string(containsString(errorMessage)));
  }

  private String asJsonString(Object obj)
  {
    try
    {
      return new ObjectMapper().writeValueAsString(obj);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
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
}
