package com.demo.api.demo_api.Controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.api.demo_api.helper.ApiResponse;
import com.demo.api.demo_api.helper.Pagination;
import com.demo.api.demo_api.helper.exception.CustomException;
import com.demo.api.demo_api.models.User;
import com.demo.api.demo_api.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
public class AuthController {
  @Autowired
  private AuthService authService;

  @RequestMapping("/users")
  @PreAuthorize("hasRole('admin')")
  public ResponseEntity<ApiResponse> index(
      @RequestParam(name = "page", required = false, defaultValue = "1") int page,
      @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage) {
    try {
      Pagination<User> users = authService.getUsers(page, perPage);
      return ResponseEntity.ok(new ApiResponse("success", users.getData(), null, users.getMeta()));
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponse("error", null, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping("/all-users")
  @PreAuthorize("hasRole('admin')")
  public ResponseEntity<ApiResponse> allUsers() {
    try {
      return ResponseEntity.ok(new ApiResponse("success", authService.getUsersAll()));
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponse("error", null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping("/profile")
  public ResponseEntity<ApiResponse> profile(HttpServletRequest request) {
    try {
      UUID userId = (UUID) request.getAttribute("userId");

      if (userId == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ApiResponse("error", "Unauthorized: No userId found in request"));
      }
      return ResponseEntity.ok(new ApiResponse("success", authService.getUserById(userId)));
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
      return new ResponseEntity<>(new ApiResponse("Success Register", registeredUser), HttpStatus.CREATED);
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
    String token = authService.login(user.getEmail(), user.getPassword());
    Object data = Map.of("token", token);
    return ResponseEntity.ok(new ApiResponse("Success Login", data));
  }
}
