package com.demo.api.demo_api.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDTO {

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Description is required")
  private String description;

  @NotNull(message = "Price cannot be null")
  @Min(value = 0, message = "Price must be greater than or equal to 0")
  private BigDecimal price;

  @NotNull(message = "Quantity cannot be null")
  @Min(value = 0, message = "Quantity must be greater than or equal to 0")
  private Integer quantity;
}
