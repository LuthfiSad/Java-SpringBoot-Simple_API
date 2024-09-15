package com.demo.api.demo_api.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.api.demo_api.helper.exception.CustomException;
import com.demo.api.demo_api.models.User;
import com.demo.api.demo_api.repository.AuthRepository;

@Service
public class AuthService {

  @Autowired
  private AuthRepository userRepository;

  public User register(User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new CustomException("Email already exists");
    }
    user.setId(UUID.randomUUID());
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    return userRepository.save(user);
  }

  public Optional<User> login(String email, String password) {
    Optional<User> user = userRepository.findByEmail(email).filter(us -> us.getPassword().equals(password));
    if (!user.isPresent()) {
      throw new CustomException("Login failed");
    }
    return user;
  }

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public Optional<User> getUserById(UUID id) {
    if (!userRepository.findById(id).isPresent()) {
      throw new CustomException("User not found");
    }
    return userRepository.findById(id);
  }
}
