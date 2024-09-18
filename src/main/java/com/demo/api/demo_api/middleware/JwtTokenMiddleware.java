package com.demo.api.demo_api.middleware;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.demo.api.demo_api.helper.ApiResponse;
import com.demo.api.demo_api.helper.jwtUtil;
import com.demo.api.demo_api.helper.exception.CustomException;
import com.demo.api.demo_api.models.User;
import com.demo.api.demo_api.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenMiddleware implements HandlerInterceptor { // Implement HandlerInterceptor

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenMiddleware.class);

  @Autowired
  private jwtUtil jwtUtil;

  @Autowired
  private AuthService authService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      sendErrorResponse(response, "Missing or invalid Authorization header");
      return false;
    }

    try {
      String token = authHeader.substring(7);
      UUID userId = jwtUtil.parseUserId(token);

      // Retrieve user from the database
      Optional<User> optionalUser = authService.getUserById(userId);
      logger.info("kosong:" + optionalUser.isEmpty());
      User user = optionalUser.get();

      // Set Authentication in the SecurityContext
      List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
      Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(auth);

      request.setAttribute("userId", userId);
    } catch (CustomException e) {
      // Handle specific custom exceptions
      sendErrorResponse(response, e.getMessage());
      return false;
    } catch (Exception e) { // Handle other unexpected exceptions
      sendErrorResponse(response, "An unexpected error occurred");
      return false;
    }

    return true;
  }

  // Method to send a JSON error response
  private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    // Create JSON response
    String jsonResponse = new ObjectMapper().writeValueAsString(new ApiResponse("Unauthorized", null, message));
    response.getWriter().write(jsonResponse);
  }
}
