package com.example.server.services.auxServices;

import java.io.IOException;

public interface IImageStorageService
{
  String saveImage(byte[] imageData) throws IOException;
  void deleteImage(String imagePath);
  String getBaseDirectory();
  byte[] extractImage(String imagePath);
}
