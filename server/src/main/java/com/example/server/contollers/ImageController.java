package com.example.server.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageController {

  private static final Path IMAGE_DIR = Paths.get("server/images").toAbsolutePath().normalize();
  private static final Path DEFAULT_IMAGE_PATH = Paths.get(
      "/Users/aleksandra/Documents/GitHub/SEP3/server/images/icons/noimage.png").toAbsolutePath();
  private static final Path DEFAULT_LOGO_PATH = Paths.get(
      "/Users/aleksandra/Documents/GitHub/SEP3/server/images/icons/cart.jpg").toAbsolutePath();

  @GetMapping("/server/images/{filename:.+}")
  public ResponseEntity<Resource> getImage(
      @PathVariable String filename,
      @RequestParam(value = "type", required = false, defaultValue = "image") String type) {
    try {
      // Resolve the requested file path
      Path imagePath = IMAGE_DIR.resolve(filename).normalize();
      System.out.println("Requested image path: " + imagePath);

      // Ensure the file is within the expected directory
      if (!imagePath.startsWith(IMAGE_DIR)) {
        throw new IllegalArgumentException("Resource not found.");
      }

      // Create a resource pointing to the requested file
      Resource resource = new UrlResource(imagePath.toUri());

      // If the requested file doesn't exist or isn't readable, use the appropriate default image
      if (!resource.exists() || !resource.isReadable()) {
        System.out.println("Requested image not found or unreadable. Falling back to default image.");
        if ("logo".equalsIgnoreCase(type)) {
          resource = new UrlResource(DEFAULT_LOGO_PATH.toUri());
          imagePath = DEFAULT_LOGO_PATH;
        } else {
          resource = new UrlResource(DEFAULT_IMAGE_PATH.toUri());
          imagePath = DEFAULT_IMAGE_PATH;
        }

        // Check again if the default image is readable
        if (!resource.exists() || !resource.isReadable()) {
          throw new IllegalArgumentException("Default image not found or unreadable.");
        }
      }

      // Dynamically determine the content type
      String contentType = Files.probeContentType(imagePath);
      if (contentType == null) {
        contentType = "application/octet-stream"; // Fallback
      }

      // Return the file with appropriate headers
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imagePath.getFileName() + "\"")
          .header(HttpHeaders.CONTENT_TYPE, contentType)
          .body(resource);
    } catch (Exception e) {
      e.printStackTrace(); // Log the exception
      throw new IllegalArgumentException("Couldn't retrieve the image");
    }
  }
}
