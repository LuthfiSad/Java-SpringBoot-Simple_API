package com.demo.api.demo_api.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.demo.api.demo_api.models.Product;

public interface ProductRepository extends MongoRepository<Product, UUID> {

}
