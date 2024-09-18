package com.demo.api.demo_api.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.api.demo_api.helper.exception.CustomException;
import com.demo.api.demo_api.helper.Pagination;
import com.demo.api.demo_api.helper.jwtUtil;
import com.demo.api.demo_api.models.User;
import com.demo.api.demo_api.repository.AuthRepository;

@Service
public class AuthService {

  @Autowired
  private AuthRepository userRepository;

  @Autowired
  private jwtUtil jwtUtil;

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public User register(User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new CustomException("Email already exists");
    }
    user.setId(UUID.randomUUID());
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());

    // Encrypt password
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public String login(String email, String password) {
    Optional<User> user = userRepository.findByEmail(email);
    if (!user.isPresent() || !passwordEncoder.matches(password, user.get().getPassword())) {
      throw new CustomException("Login failed");
    }
    String token = jwtUtil.generateToken(user.get().getId());
    return token;
  }

  public Pagination<User> getUsers(int page, int perPage) {
    Pageable pageable = PageRequest.of(page - 1, perPage);
    Page<User> users = userRepository.findAll(pageable);

    int totalPages = users.getTotalPages();
    int totalData = (int) users.getTotalElements();

    Pagination.Meta meta = new Pagination.Meta(perPage, page, totalPages, totalData);

    return new Pagination<>(users.getContent(), meta);
  }

  public List<User> getUsersAll() {
    return userRepository.findAll();
  }

  public Optional<User> getUserById(UUID id) {
    if (!userRepository.findById(id).isPresent()) {
      throw new CustomException("User not found");
    }
    return userRepository.findById(id);
  }
}
