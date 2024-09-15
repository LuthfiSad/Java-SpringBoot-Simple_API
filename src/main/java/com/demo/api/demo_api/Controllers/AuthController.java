package com.demo.api.demo_api.Controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.api.demo_api.helper.ApiResponse;
import com.demo.api.demo_api.helper.exception.CustomException;
import com.demo.api.demo_api.models.User;
import com.demo.api.demo_api.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
public class AuthController {
  @Autowired
  private AuthService authService;

  @RequestMapping("/users")
  public ResponseEntity<ApiResponse> index() {
    return ResponseEntity.ok(new ApiResponse("success", authService.getUsers()));
  }

  @RequestMapping("/profile/{id}")
  public ResponseEntity<ApiResponse> profile(@PathVariable UUID id) {
    try {
      return ResponseEntity.ok(new ApiResponse("success", authService.getUserById(id)));
    } catch (CustomException e) {
      return ResponseEntity.badRequest().body(new ApiResponse("Validation Error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("error", "An unexpected error occurred"));
    }
  }

  @PostMapping("/auth/register")
  public ResponseEntity<ApiResponse> register(@Valid @RequestBody User user) {
    try {
      User registeredUser = authService.register(user);
      return ResponseEntity.ok(new ApiResponse("success", registeredUser));
    } catch (CustomException e) {
      return ResponseEntity.badRequest().body(new ApiResponse("Validation Error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("error", "An unexpected error occurred"));
    }
  }

  @PostMapping("/auth/login")
  public ResponseEntity<ApiResponse> login(@Valid @RequestBody Map<String, String> loginData) {
    User user = User.loginUser(loginData.get("email"), loginData.get("password"));
    return ResponseEntity.ok(new ApiResponse("success", authService.login(user.getEmail(), user.getPassword())));
  }
}
