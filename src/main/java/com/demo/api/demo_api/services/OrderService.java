package com.demo.api.demo_api.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.api.demo_api.helper.exception.CustomException;
import com.demo.api.demo_api.helper.validatorCostum.UUIDValidator;
import com.demo.api.demo_api.models.Order;
import com.demo.api.demo_api.models.Product;
import com.demo.api.demo_api.models.User;
import com.demo.api.demo_api.repository.AuthRepository;
import com.demo.api.demo_api.repository.OrderRepository;
import com.demo.api.demo_api.repository.ProductRepository;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private AuthRepository userRepository;

  @Autowired
  private UUIDValidator uuidValidator;

  // Create order
  public Order createOrder(UUID userId, Order orderRequest) {
    // Find product based on productId in the order request
    Product product = productRepository.findById(orderRequest.getProductId())
        .orElseThrow(() -> new CustomException("Product not found"));

    // Validate product quantity
    if (product.getQuantity() < orderRequest.getQuantity()) {
      throw new CustomException("Not enough stock for product");
    }

    // Calculate total price
    BigDecimal totalPrice = product.getPrice().multiply(new BigDecimal(orderRequest.getQuantity()));

    Order order = new Order(userId, orderRequest, totalPrice);

    // Decrease product stock
    product.setQuantity(product.getQuantity() - orderRequest.getQuantity());
    productRepository.save(product); // Update product quantity in the database

    // Save and return the order
    return orderRepository.save(order);
  }

  // Find all orders for a user
  public List<Order> getOrdersByUserId(UUID userId) {
    List<Order> orders = orderRepository.findByUserId(userId);
    List<Order> orderResponses = new ArrayList<>();

    for (Order order : orders) {
      // Ambil data product dan user
      Product product = productRepository.findById(order.getProductId())
          .orElseThrow(() -> new CustomException("Product not found"));
      User user = userRepository.findById(order.getUserId())
          .orElseThrow(() -> new CustomException("User not found"));

      // Tambahkan ke dalam response list
      Order orderResponse = new Order(order, product, user);
      orderResponses.add(orderResponse);
    }

    return orderResponses;
  }
  // public List<Order> getOrdersByUserId(UUID userId) {
  // return orderRepository.findByUserId(userId);
  // }

  // Find all orders for a specific product for a user
  public List<Order> getOrdersByUserIdAndProductId(UUID userId, String productIdStr) {
    UUID productId = uuidValidator.validate(productIdStr);

    // Validasi apakah product ada
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomException("Product not found"));

    // Cari semua order berdasarkan userId dan productId
    List<Order> orders = orderRepository.findByUserIdAndProductId(userId, productId);
    List<Order> orderResponses = new ArrayList<>();

    for (Order order : orders) {
      // Ambil data user
      User user = userRepository.findById(order.getUserId())
          .orElseThrow(() -> new CustomException("User not found"));

      // Tambahkan ke dalam response list
      Order orderResponse = new Order(order, product, user);
      orderResponses.add(orderResponse);
    }

    return orderResponses;
  }
  // public List<Order> getOrdersByUserIdAndProductId(UUID userId, String
  // productIdStr) {
  // UUID productId = uuidValidator.validate(productIdStr);
  // if (!productRepository.findById(productId).isPresent()) {
  // throw new CustomException("Product not found");
  // }
  // return orderRepository.findByUserIdAndProductId(userId, productId);
  // }

  // Find order by ID
  public Order getOrderById(String orderIdStr) {
    UUID orderId = uuidValidator.validate(orderIdStr);

    // Temukan order berdasarkan ID
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException("Order not found"));

    // Ambil data product dan user
    Product product = productRepository.findById(order.getProductId())
        .orElseThrow(() -> new CustomException("Product not found"));
    User user = userRepository.findById(order.getUserId())
        .orElseThrow(() -> new CustomException("User not found"));

    // Kembalikan response dengan order, product, dan user
    return new Order(order, product, user);
  }
  // public Order getOrderById(String orderIdStr) {
  // UUID orderId = uuidValidator.validate(orderIdStr);
  // return orderRepository.findById(orderId)
  // .orElseThrow(() -> new CustomException("Order not found"));
  // }
}
