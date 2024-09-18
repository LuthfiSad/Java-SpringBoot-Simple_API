package com.demo.api.demo_api.helper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.demo.api.demo_api.helper.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
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

}
