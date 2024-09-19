package com.demo.api.demo_api.helper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.demo.api.demo_api.helper.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    return ResponseEntity.badRequest().body(new ApiResponse("error", "Invalid request data"));
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse> handleCustomException(CustomException ex) {
    return ResponseEntity.badRequest().body(new ApiResponse("error", null, ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    // Ambil error pertama dari list error yang ada
    FieldError firstError = (FieldError) ex.getBindingResult().getAllErrors().get(0);

    // Ambil pesan error pertama
    String errorMessage = firstError.getDefaultMessage();

    // Kembalikan response hanya dengan error pertama
    return new ResponseEntity<>(new ApiResponse("Validation Error", null, errorMessage), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ApiResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
    ApiResponse response = new ApiResponse("error", "Endpoint not found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiResponse> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    ApiResponse response = new ApiResponse("error", "HTTP method not supported");
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse> handleGenericException(Exception e) {
    ApiResponse response = new ApiResponse("error", "An unexpected error occurred");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
