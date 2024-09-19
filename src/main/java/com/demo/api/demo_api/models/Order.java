package com.demo.api.demo_api.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Document(collection = "orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
  @Id
  private UUID id;

  private UUID userId;
  private User user;

  @NotNull(message = "Product ID is required")
  private UUID productId;
  private Product product;

  @NotNull(message = "Quantity is required")
  private Integer quantity;

  private BigDecimal totalPrice;

  public enum OrderStatus {
    cancel,
    paid,
    unpaid
  }

  private OrderStatus status;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Order() {
    this.id = UUID.randomUUID();
  }

  public Order(UUID userId, Order order, BigDecimal totalPrice) {
    this();
    this.userId = userId;
    this.productId = order.getProductId();
    this.quantity = order.getQuantity();
    this.totalPrice = totalPrice;
    this.status = OrderStatus.unpaid;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public Order(Order order, Product product, User user) {
    this.id = order.getId();
    this.userId = order.getUserId();
    this.productId = order.getProductId();
    this.quantity = order.getQuantity();
    this.totalPrice = order.getTotalPrice();
    this.status = order.getStatus();
    this.product = product;
    this.user = user;
  }
}
