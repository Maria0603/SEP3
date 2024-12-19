package com.example.server.services.auxServices;

import java.io.IOException;

public interface IImageStorageService
{
  String saveImage(byte[] imageData) throws IOException;
  void deleteImage(String imagePath);
  void updateImage(String imagePath, byte[] newImageData) throws IOException;
}
