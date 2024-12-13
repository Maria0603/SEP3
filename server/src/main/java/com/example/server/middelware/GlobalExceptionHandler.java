package com.example.server.middelware;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice public class GlobalExceptionHandler
{

  @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<String> handleValidationErrors(
      MethodArgumentNotValidException ex)
  {
    ex.printStackTrace();
    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.joining(", "));
    return ResponseEntity.badRequest().body(errorMessage);
  }

  @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<String> handleIllegalArgumentException(
      IllegalArgumentException ex)
  {
    ex.printStackTrace();
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(Exception.class) public ResponseEntity<String> handleGeneralException(
      Exception ex)
  {
    ex.printStackTrace();
    return ResponseEntity.badRequest().body("An unexpected error occurred: " + ex.getMessage());
  }
}

