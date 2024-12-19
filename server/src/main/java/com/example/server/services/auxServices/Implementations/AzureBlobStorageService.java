package com.example.server.services.auxServices.Implementations;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.server.config.AzureBlobStorageConfig;
import com.example.server.services.auxServices.IImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class AzureBlobStorageService implements IImageStorageService
{


    private final BlobContainerClient blobContainerClient;


    public AzureBlobStorageService(AzureBlobStorageConfig azureBlobStorageConfig) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(azureBlobStorageConfig.getConnectionString())
                .buildClient();
        this.blobContainerClient = blobServiceClient.getBlobContainerClient(azureBlobStorageConfig.getContainerName());
    }

    @Override
    public String saveImage(byte[] imageData) throws IOException {
        if (imageData == null || imageData.length == 0) {
            throw new IllegalArgumentException("Image data cannot be null or empty");
        }

        // Generate a unique file name for the blob
        String uniqueFileName = UUID.randomUUID().toString();
        BlobClient blobClient = blobContainerClient.getBlobClient(uniqueFileName);

        // Upload the image as a byte array
        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(imageData)) {
            blobClient.upload(dataStream, imageData.length, true);
        }

        // Return the blob name instead of the blob URL
        return uniqueFileName;
    }
@Override
    public void updateImage(String imagePath, byte[] newImageData) throws IOException {
        if (newImageData == null || newImageData.length == 0) {
            throw new IllegalArgumentException("Image data cannot be null or empty");
        }

        String blobName = extractBlobName(imagePath);
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

        // Delete the existing blob (if it exists)
        blobClient.deleteIfExists();

        // Upload the new image data
        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(newImageData)) {
            blobClient.upload(dataStream, newImageData.length, true);
        }
    }


    @Override
    public void deleteImage(String imagePath) {
        String blobName = extractBlobName(imagePath);
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        blobClient.deleteIfExists();
    }

    public String extractBlobName(String imagePath) {
        // Assuming imagePath is the blob URL, extract the blob name
        return imagePath.substring(imagePath.lastIndexOf("/") + 1);
    }

    @Override
    public String getBaseDirectory() {
        return "";
    }

    @Override
    public byte[] extractImage(String imagePath) {
        return new byte[0];
    }
}
