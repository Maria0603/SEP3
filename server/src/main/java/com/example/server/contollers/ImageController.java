package com.example.server.contollers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController public class ImageController
{
  private static final Path IMAGE_DIR = Paths.get("server/images")
      .toAbsolutePath().normalize();

  @GetMapping("/server/images/{filename:.+}") public ResponseEntity<Resource> getImage(
      @PathVariable String filename)
  {
    try
    {
      // Resolve the requested file path
      Path imagePath = IMAGE_DIR.resolve(filename).normalize();
      System.out.println("image path:" + imagePath);

      // Ensure the file is within the expected directory
      if (!imagePath.startsWith(IMAGE_DIR))
        throw new IllegalArgumentException("Resource not found.");

      // Create a resource pointing to the file
      Resource resource = new UrlResource(imagePath.toUri());

      // Ensure the file exists and is readable
      if (!resource.exists() || !resource.isReadable())
        throw new IllegalArgumentException("Resource not found.");

      // Dynamically determine the content type
      String contentType = Files.probeContentType(imagePath);
      if (contentType == null)
        contentType = "application/octet-stream"; // Fallback

      // Return the file with appropriate headers
      return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
              "inline; filename=\"" + filename + "\"")
          .header(HttpHeaders.CONTENT_TYPE, contentType).body(resource);
    }
    catch (Exception e)
    {
      e.printStackTrace(); // We need a logger
      throw new IllegalArgumentException("Couldn't retrieve the image");
    }
  }
}

