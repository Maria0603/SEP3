import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.example.server.config.AzureBlobStorageConfig;
import com.example.server.services.auxServices.Implementations.AzureBlobStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ImageControllerTest {

    @Mock
    private AzureBlobStorageConfig azureBlobStorageConfig; // Mock the configuration

    @Mock
    private BlobServiceClient blobServiceClient; // Mock the BlobServiceClient

    @Mock
    private BlobContainerClient blobContainerClient; // Mock the BlobContainerClient

    @Mock
    private BlobClient blobClient; // Mock the BlobClient

    @InjectMocks
    private AzureBlobStorageService azureBlobStorageService; // Inject mocks into the service

    private byte[] imageData;

    @BeforeEach
    public void setUp() {
        // Open mocks for all mocked dependencies
        MockitoAnnotations.openMocks(this);

        // Mock the behavior of AzureBlobStorageConfig to return valid connection string and container name
        when(azureBlobStorageConfig.getConnectionString()).thenReturn("DefaultEndpointsProtocol=https;AccountName=ewimagestorage;AccountKey=u115pNA/j/zBlv8l8H8VYQem6g/og99a6Gz/QmGI2tw43DfrrA35+bXmYg96QApGNvUyP5U0AI0M+ASt2pKQ7w==;EndpointSuffix=core.windows.net");
        when(azureBlobStorageConfig.getContainerName()).thenReturn("images");

        // Mock the BlobServiceClient to return a BlobContainerClient
        when(blobServiceClient.getBlobContainerClient(anyString())).thenReturn(blobContainerClient);

        // Mock the BlobContainerClient to return a BlobClient
        when(blobContainerClient.getBlobClient(anyString())).thenReturn(blobClient);

        // Set up some sample image data
        imageData = new byte[]{1, 2, 3, 4};
    }

    @Test
    void saveImage_ShouldReturnBlobName_WhenImageDataIsValid() throws IOException {
        // Arrange
        String expectedBlobName = UUID.randomUUID().toString();
        when(blobClient.getBlobName()).thenReturn(expectedBlobName);

        // Act
        String blobName = azureBlobStorageService.saveImage(imageData);

        // Assert
        assertNotNull(blobName);
        assertEquals(expectedBlobName, blobName);
        // Verify that the upload method was called once with correct arguments
        verify(blobClient, times(1)).upload(any(ByteArrayInputStream.class), eq(imageData.length), eq(true));
    }

    @Test
    void updateImage_ShouldReplaceExistingImage_WhenNewImageDataIsValid() throws IOException {
        // Arrange
        String oldImagePath = "old-image.jpg";
        byte[] newImageData = new byte[]{5, 6, 7, 8};
        String blobName = "old-image.jpg";

        // Mock the behavior for BlobClient
        when(blobContainerClient.getBlobClient(blobName)).thenReturn(blobClient);

        // Act
        azureBlobStorageService.updateImage(oldImagePath, newImageData);

        // Assert
        verify(blobClient, times(1)).deleteIfExists(); // Ensure the old image is deleted
        verify(blobClient, times(1)).upload(any(ByteArrayInputStream.class), eq(newImageData.length), eq(true)); // Ensure the new image is uploaded
    }

    @Test
    void deleteImage_ShouldDeleteExistingImage() {
        // Arrange
        String imagePath = "image-to-delete.jpg";
        String blobName = "image-to-delete.jpg";

        // Mock the behavior for BlobClient
        when(blobContainerClient.getBlobClient(blobName)).thenReturn(blobClient);

        // Act
        azureBlobStorageService.deleteImage(imagePath);

        // Assert
        verify(blobClient, times(1)).deleteIfExists(); // Ensure the image is deleted
    }

    @Test
    void extractBlobName_ShouldReturnCorrectBlobName() {
        // Arrange
        String imagePath = "https://myaccount.blob.core.windows.net/mycontainer/old-image.jpg";

        // Act
        String blobName = azureBlobStorageService.extractBlobName(imagePath);

        // Assert
        assertEquals("old-image.jpg", blobName); // Assert that the blob name was extracted correctly
    }
}
