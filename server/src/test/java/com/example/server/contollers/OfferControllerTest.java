package com.example.server.contollers;

import com.example.sep3.grpc.CreateOfferRequest;
import com.example.sep3.grpc.OfferResponse;
import com.example.server.DataServerStub;
import com.example.server.dto.offer.CreateOfferRequestDto;
import com.example.server.dto.offer.OfferResponseDto;
import com.example.server.services.auxServices.IImageStorageService;
import com.example.server.services.Implementations.OfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OfferServiceTest {

    @Mock
    private DataServerStub dataServerStub;

    @Mock
    private IImageStorageService imageStorageService;

    @InjectMocks
    private OfferService offerService;

    private CreateOfferRequestDto validRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up a valid request DTO
        validRequestDto = new CreateOfferRequestDto();
        validRequestDto.setTitle("Test Offer");
        validRequestDto.setDescription("Test Description");
        validRequestDto.setOriginalPrice(100);
        validRequestDto.setOfferPrice(50);
        validRequestDto.setNumberOfItems(10);
        validRequestDto.setPickupTimeStart(LocalDateTime.now().plusDays(1));
        validRequestDto.setImage(new byte[]{1, 2, 3}); // Mock image data
    }

    @Test
    void createOffer_Success() throws IOException {
        // Mock image storage service
        when(imageStorageService.saveImage(any(byte[].class))).thenReturn("mock/path/image.png");

        // Mock gRPC response
        OfferResponse mockResponse = OfferResponse.newBuilder()
                .setId("12345")
                .setTitle("Test Offer")
                .setDescription("Test Description")
                .build();
        when(dataServerStub.createOffer(any(CreateOfferRequest.class))).thenReturn(mockResponse);

        // Call the service method
        OfferResponseDto result = offerService.createOffer(validRequestDto, "user123");

        // Verify behavior
        verify(imageStorageService).saveImage(validRequestDto.getImage());
        verify(dataServerStub).createOffer(any(CreateOfferRequest.class));

        // Assert results
        assertEquals("12345", result.getId());
        assertEquals("Test Offer", result.getTitle());
    }

    @Test
    void createOffer_ImageSaveFails() throws IOException {
        // Mock exception on image saving
        when(imageStorageService.saveImage(any(byte[].class))).thenThrow(new IOException("Failed to save image"));

        // Verify exception is thrown
        assertThrows(IllegalArgumentException.class,
                () -> offerService.createOffer(validRequestDto, "user123"));

        // Verify no gRPC call is made
        verify(dataServerStub, never()).createOffer(any(CreateOfferRequest.class));
    }
}
