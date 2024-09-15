package com.demo.api.demo_api.repository;

import java.util.Optional;
import java.util.UUID;

import com.demo.api.demo_api.models.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthRepository extends MongoRepository<User, UUID> {
  Optional<User> findByEmail(String email);
}
