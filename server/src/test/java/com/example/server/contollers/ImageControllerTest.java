package com.example.server.contollers;

class ImageControllerTest
{

  /*private ImageStorageService imageStorageService;
  private ImageController imageController;

  @BeforeEach void setUp()
  {
    imageStorageService = mock(ImageStorageService.class);
    imageController = new ImageController(imageStorageService);
  }

  @Test void uploadImage_NoFileProvided_ShouldReturnInternalServerError()
  {
    UploadImageRequestDto requestDto = new UploadImageRequestDto();
    requestDto.setFile(null);
    requestDto.setCategory(Category.BREAD_AND_CAKE);

    ResponseEntity<String> response = imageController.uploadImage(requestDto);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertTrue(response.getBody().contains("Error saving image"));
  }

  @Test void uploadImage_OneFile_ShouldReturnOk() throws IOException
  {
    MultipartFile file = mock(MultipartFile.class);
    when(file.getBytes()).thenReturn("TestImageData".getBytes());
    when(file.getOriginalFilename()).thenReturn("image.jpg");

    UploadImageRequestDto requestDto = new UploadImageRequestDto();
    requestDto.setFile(file);
    requestDto.setCategory(Category.VEGAN);

    when(imageStorageService.saveImage(any(), any(), any())).thenReturn(
        "/path/to/image.jpg");

    ResponseEntity<String> response = imageController.uploadImage(requestDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().contains("/path/to/image.jpg"));

    verify(imageStorageService, times(1)).saveImage(file.getBytes(),
        Category.VEGAN, "image.jpg");
  }

  @Test void uploadImage_ManyFiles_ShouldHandleEachCorrectly()
      throws IOException
  {
    MultipartFile file1 = mock(MultipartFile.class);
    when(file1.getBytes()).thenReturn("Image1Data".getBytes());
    when(file1.getOriginalFilename()).thenReturn("image1.jpg");

    MultipartFile file2 = mock(MultipartFile.class);
    when(file2.getBytes()).thenReturn("Image2Data".getBytes());
    when(file2.getOriginalFilename()).thenReturn("image2.jpg");

    UploadImageRequestDto requestDto1 = new UploadImageRequestDto();
    requestDto1.setFile(file1);
    requestDto1.setCategory(Category.VEGETARIAN);

    UploadImageRequestDto requestDto2 = new UploadImageRequestDto();
    requestDto2.setFile(file2);
    requestDto2.setCategory(Category.MEAL);

    when(imageStorageService.saveImage(any(), any(), any())).thenReturn(
        "/path/to/image1.jpg", "/path/to/image2.jpg");

    ResponseEntity<String> response1 = imageController.uploadImage(requestDto1);
    ResponseEntity<String> response2 = imageController.uploadImage(requestDto2);

    assertEquals(HttpStatus.OK, response1.getStatusCode());
    assertTrue(response1.getBody().contains("/path/to/image1.jpg"));

    assertEquals(HttpStatus.OK, response2.getStatusCode());
    assertTrue(response2.getBody().contains("/path/to/image2.jpg"));

    verify(imageStorageService, times(1)).saveImage(file1.getBytes(),
        Category.VEGETARIAN, "image1.jpg");
    verify(imageStorageService, times(1)).saveImage(file2.getBytes(),
        Category.MEAL, "image2.jpg");
  }

  @Test void uploadImage_LargeFile_ShouldHandleGracefully() throws IOException
  {
    MultipartFile file = mock(MultipartFile.class);
    byte[] largeData = new byte[10 * 1024 * 1024]; // 10 MB file
    when(file.getBytes()).thenReturn(largeData);
    when(file.getOriginalFilename()).thenReturn("large_image.jpg");

    UploadImageRequestDto requestDto = new UploadImageRequestDto();
    requestDto.setFile(file);
    requestDto.setCategory(Category.GROCERIES);

    when(imageStorageService.saveImage(any(), any(), any())).thenReturn(
        "/path/to/large_image.jpg");

    ResponseEntity<String> response = imageController.uploadImage(requestDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().contains("/path/to/large_image.jpg"));

    verify(imageStorageService, times(1)).saveImage(largeData,
        Category.GROCERIES, "large_image.jpg");
  }

  @Test void uploadImage_SaveThrowsIOException_ShouldReturnInternalServerError()
      throws IOException
  {
    MultipartFile file = mock(MultipartFile.class);
    when(file.getBytes()).thenReturn("TestImageData".getBytes());
    when(file.getOriginalFilename()).thenReturn("error_image.jpg");

    UploadImageRequestDto requestDto = new UploadImageRequestDto();
    requestDto.setFile(file);
    requestDto.setCategory(Category.OTHER);

    when(imageStorageService.saveImage(any(), any(), any())).thenThrow(
        new IOException("Disk full"));

    ResponseEntity<String> response = imageController.uploadImage(requestDto);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertTrue(response.getBody().contains("Disk full"));

    verify(imageStorageService, times(1)).saveImage(file.getBytes(),
        Category.OTHER, "error_image.jpg");
  }*/

}
