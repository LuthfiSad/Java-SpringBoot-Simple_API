package com.demo.api.demo_api.Controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.api.demo_api.helper.ApiResponse;
import com.demo.api.demo_api.helper.exception.CustomException;
import com.demo.api.demo_api.models.Order;
import com.demo.api.demo_api.services.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  // Create a new order
  @PostMapping
  public ResponseEntity<ApiResponse> createOrder(@Valid @RequestBody Order orderRequest, HttpServletRequest request) {
    try {
      UUID userId = (UUID) request.getAttribute("userId");
      Order order = orderService.createOrder(userId, orderRequest);
      return new ResponseEntity<>(new ApiResponse("success", order), HttpStatus.CREATED);
    } catch (CustomException e) {
      return ResponseEntity.badRequest().body(new ApiResponse("Validation Error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("error", "An unexpected error occurred"));
    }
  }

  // Get all orders for the current user
  @GetMapping
  public ResponseEntity<ApiResponse> getOrders(HttpServletRequest request) {
    try {
      UUID userId = (UUID) request.getAttribute("userId");
      List<Order> orders = orderService.getOrdersByUserId(userId);
      return new ResponseEntity<>(new ApiResponse("success", orders), HttpStatus.OK);
    } catch (CustomException e) {
      return ResponseEntity.badRequest().body(new ApiResponse("Validation Error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("error", "An unexpected error occurred"));
    }
  }

  // Get all orders for a specific product for the current user
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getOrdersByProductId(@PathVariable("id") String productIdStr,
      HttpServletRequest request) {
    System.out.println(productIdStr);
    try {
      UUID userId = (UUID) request.getAttribute("userId");
      List<Order> orders = orderService.getOrdersByUserIdAndProductId(userId, productIdStr);
      return new ResponseEntity<>(new ApiResponse("success", orders), HttpStatus.OK);
    } catch (CustomException e) {
      return ResponseEntity.badRequest().body(new ApiResponse("Validation Error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("error", "An unexpected error occurred"));
    }
  }

  // Get details of a specific order by order ID
  @GetMapping("/detail/{id}")
  public ResponseEntity<ApiResponse> getOrderDetail(@PathVariable("id") String orderIdStr) {
    try {
      Order order = orderService.getOrderById(orderIdStr);
      return new ResponseEntity<>(new ApiResponse("success", order), HttpStatus.OK);
    } catch (CustomException e) {
      return ResponseEntity.badRequest().body(new ApiResponse("Validation Error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("error", "An unexpected error occurred"));
    }
  }
}
