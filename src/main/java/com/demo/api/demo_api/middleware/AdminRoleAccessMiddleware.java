package com.demo.api.demo_api.middleware;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.api.demo_api.helper.ApiResponse;

@Component
public class AdminRoleAccessMiddleware implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getAuthorities() == null) {
      sendErrorResponse(response, "Unauthorized: No Authentication");
      return false;
    }

    boolean hasAdminRole = authentication.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("admin"));

    if (!hasAdminRole) {
      sendErrorResponse(response, "Forbidden: Admin role required");
      return false;
    }

    return true;
  }

  private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String jsonResponse = new ObjectMapper().writeValueAsString(new ApiResponse("error", message));
    response.getWriter().write(jsonResponse);
  }
}
