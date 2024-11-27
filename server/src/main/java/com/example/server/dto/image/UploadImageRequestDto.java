package com.example.server.dto.image;

import com.example.shared.model.Category;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class UploadImageRequestDto
{

  @NotNull(message = "The image is required.") private MultipartFile file;
  @NotNull(message = "At least one category must be provided.") private Category category;

  public @NotNull(message = "The image is required.") MultipartFile getFile()
  {
    return file;
  }

  public void setFile(
      @NotNull(message = "The image is required.") MultipartFile file)
  {
    this.file = file;
  }

  public @NotNull(message = "At least one category must be provided.") Category getCategory()
  {
    return category;
  }

  public void setCategory(
      @NotNull(message = "At least one category must be provided.") Category category)
  {
    this.category = category;
  }

}
