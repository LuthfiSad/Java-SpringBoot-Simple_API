package com.demo.api.demo_api.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.demo.api.demo_api.DTO.ProductDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Document(collection = "products")
public class Product {
  @Id
  private UUID id;

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Description is required")
  private String description;

  @NotNull(message = "Price cannot be null")
  @Positive(message = "Price must be positive")
  private BigDecimal price;

  @NotBlank(message = "Image is required")
  private String image;

  @NotNull(message = "Quantity cannot be null")
  @Positive(message = "Quantity must be positive")
  private Integer quantity;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Product() {
    this.id = UUID.randomUUID();
  }

  public Product(ProductDTO productDTO, String imagePath) {
    this();
    this.setName(productDTO.getName());
    this.setDescription(productDTO.getDescription());
    this.setPrice(productDTO.getPrice());
    this.setQuantity(productDTO.getQuantity());
    this.setImage(imagePath);
    this.setCreatedAt(LocalDateTime.now());
    this.setUpdatedAt(LocalDateTime.now());
  }
}
