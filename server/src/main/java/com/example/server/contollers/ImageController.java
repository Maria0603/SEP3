package com.example.server.contollers;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.server.config.AzureBlobStorageConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController
{
  private final BlobContainerClient blobContainerClient;

  public ImageController(AzureBlobStorageConfig azureBlobStorageConfig) {
    BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .connectionString(azureBlobStorageConfig.getConnectionString())
            .buildClient();
    this.blobContainerClient = blobServiceClient.getBlobContainerClient(azureBlobStorageConfig.getContainerName());
  }

  @GetMapping("/{blobName}")
  public ResponseEntity<byte[]> getImage(@PathVariable String blobName) {
    System.out.println("Blob name: " + blobName);
    BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      blobClient.download(outputStream);
      byte[] content = outputStream.toByteArray();

      return ResponseEntity.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + blobName)
              .contentType(MediaType.IMAGE_JPEG) // Adjust based on image type
              .body(content);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}



