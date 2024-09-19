package com.demo.api.demo_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.demo.api.demo_api.models.Order;

public interface OrderRepository extends MongoRepository<Order, UUID> {
  List<Order> findByUserId(UUID userId);

  List<Order> findByUserIdAndProductId(UUID userId, UUID productId);
}
