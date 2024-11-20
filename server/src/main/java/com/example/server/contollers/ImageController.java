package com.example.server.contollers;

import com.example.server.dto.UploadImageRequestDto;
import com.example.server.services.ImageStorageService;
import com.example.shared.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController @RequestMapping("/images") public class ImageController
{

  private final ImageStorageService imageStorageService;

  public ImageController(ImageStorageService imageStorageService)
  {
    this.imageStorageService = imageStorageService;
  }

  @PostMapping(consumes = "multipart/form-data") public ResponseEntity<String> uploadImage(
      @RequestParam("file") MultipartFile file,
      @RequestParam("category") String category)
  {
    try
    {
      System.out.println("File Size: " + file.getSize());

      // Save the image using the service
      String filePath = imageStorageService.saveImage(file.getBytes(), category,
          file.getOriginalFilename());
      System.out.println("Image saved at: " + filePath);
      return ResponseEntity.ok(filePath.replace("\\", "\\\\"));
    }
    catch (IOException e)
    {
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}

