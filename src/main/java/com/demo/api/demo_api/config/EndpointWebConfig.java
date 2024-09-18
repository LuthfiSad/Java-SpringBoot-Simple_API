package com.demo.api.demo_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.demo.api.demo_api.middleware.AdminRoleAccessMiddleware;
import com.demo.api.demo_api.middleware.JwtTokenMiddleware;

@Configuration
public class EndpointWebConfig implements WebMvcConfigurer {

  @Autowired
  private JwtTokenMiddleware jwtTokenMiddleware;

  @Autowired
  private AdminRoleAccessMiddleware AdminRoleAccessMiddleware;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtTokenMiddleware)
        .addPathPatterns("/users/**", "/profile/**", "/products/**") // Middleware JWT untuk path users dan profile
        .excludePathPatterns("/products/images/**");
    registry.addInterceptor(AdminRoleAccessMiddleware)
        .addPathPatterns("/users/**"); // Only apply to paths requiring admin role
  }
}
