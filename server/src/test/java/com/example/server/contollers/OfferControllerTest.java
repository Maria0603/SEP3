package com.example.server.contollers;

import com.example.server.controllers.OfferController;
import com.example.server.dto.offer.CreateOfferRequestDto;
import com.example.server.dto.offer.OfferResponseDto;
import com.example.server.security.JWTUtils;
import com.example.server.security.UserDetailsService;
import com.example.server.services.Implementations.OfferService;
import com.example.shared.entities.userEntities.Business;
import com.example.shared.model.Address;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OfferController.class)
@AutoConfigureMockMvc
class OfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService offerService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private JWTUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateOfferRequestDto validRequestDto;
    private OfferResponseDto validResponseDto;

    private final String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkdW1teWVtYWlsMTczNDQxNzMxNkBleGFtcGxlLmNvbSIsImlhdCI6MTczNDQ0MzMyNSwiZXhwIjoxNzM1MzA3MzI1LCJyb2xlIjpbIkNVU1RPTUVSIl0sInVzZXJJZCI6IjY3NjA2NGIyYzZmZGYxNzQxZGVkNzJiMCJ9.5L9iAGwBIAgxbVJ4AMWKc-IIysM3lIBNLfLU3ZHvBLA";

    @BeforeEach
    void setUp() {
        //  Mock Categories
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Vegetarian");

        validRequestDto = new CreateOfferRequestDto();
        validRequestDto.setTitle("Test Offer");
        validRequestDto.setDescription("Test Description");
        validRequestDto.setOriginalPrice(100);
        validRequestDto.setOfferPrice(50);
        validRequestDto.setNumberOfItems(10);
        validRequestDto.setCategories(categories);
        validRequestDto.setPickupTimeStart(LocalDateTime.of(2024,12,19,12,0));
        validRequestDto.setPickupTimeEnd(LocalDateTime.of(2024,12,20,12,0));

        validResponseDto = new OfferResponseDto();
        //validResponseDto.setId("67890");
        validResponseDto.setTitle(validRequestDto.getTitle());
        validResponseDto.setDescription(validRequestDto.getDescription());
        validResponseDto.setOriginalPrice(validRequestDto.getOriginalPrice());
        validResponseDto.setOfferPrice(validRequestDto.getOfferPrice());
        validResponseDto.setNumberOfItems(validRequestDto.getNumberOfItems());
        validResponseDto.setCategories(categories);
        validResponseDto.setPickupTimeStart(validRequestDto.getPickupTimeStart());
        validResponseDto.setPickupTimeEnd(validRequestDto.getPickupTimeEnd());
    }


    @Test
    void saveOffer_validRequest_shouldReturnOkResponse() throws Exception {
        // Mock the behavior of OfferService
        when(offerService.createOffer(eq(validRequestDto), any(String.class)))
                .thenReturn(validResponseDto);

        // Perform a POST request
        mockMvc.perform(post("/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(validResponseDto.getId()))
                .andExpect(jsonPath("$.title").value(validResponseDto.getTitle()))
                .andExpect(jsonPath("$.description").value(validResponseDto.getDescription()))
                .andExpect(jsonPath("$.offerPrice").value(validResponseDto.getOfferPrice()));

        // Verify that the service was called
        verify(offerService, times(1)).createOffer(eq(validRequestDto), any(String.class));
    }

    @Test
    void shouldCreateOffer() throws Exception {
        String offerJson = """
            {
                "title": "Test Offer",
                "description": "Test Description",
                "originalPrice": 100,
                "offerPrice": 50,
                "numberOfItems": 10,
                "categories": ["Vegetarian"],
                "pickupTimeStart": "2024-12-19T12:00:00",
                "pickupTimeEnd": "2024-12-20T12:00:00",
                "image": null
            }
        """;

        mockMvc.perform(post("/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(offerJson)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }


/*    @Test
    void saveOffer_invalidRequest_shouldReturnBadRequest() throws Exception {
        // Create an invalid request (missing required fields)
        CreateOfferRequestDto invalidRequestDto = new CreateOfferRequestDto();

        // Perform a POST request
        mockMvc.perform(post("/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto))
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());

        // Verify that the service was not called
        verify(offerService, never()).createOffer(any(), any());
    }*/

/*    @Test
    void saveOffer_serviceThrowsException_shouldReturnBadRequest() throws Exception {
        // Mock the behavior of OfferService to throw an exception
        when(offerService.createOffer(eq(validRequestDto), any(String.class)))
                .thenThrow(new IllegalArgumentException("Invalid input"));

        // Perform a POST request
        mockMvc.perform(post("/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDto))
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input"));

        // Verify that the service was called
        verify(offerService, times(1)).createOffer(eq(validRequestDto), any(String.class));
    }*/



}
