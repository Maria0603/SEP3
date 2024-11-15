package com.example.server.middelware;

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
    String errorMessage = ex.getBindingResult().getFieldErrors().stream().map(
        fieldError -> fieldError.getField() + ": "
            + fieldError.getDefaultMessage()).collect(Collectors.joining(", "));
    return ResponseEntity.badRequest().body(errorMessage);
  }
}

