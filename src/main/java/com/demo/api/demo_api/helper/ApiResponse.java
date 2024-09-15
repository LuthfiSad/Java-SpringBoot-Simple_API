package com.demo.api.demo_api.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
  private String message;
  private Object data;
  private Object error; // Field untuk menyimpan error

  public ApiResponse(String message, Object data) {
    this.message = message;
    this.data = data;
  }

  public ApiResponse(String message, Object data, Object error) {
    this.message = message;
    this.data = data;
    this.error = error; // Inisialisasi error
  }
}
