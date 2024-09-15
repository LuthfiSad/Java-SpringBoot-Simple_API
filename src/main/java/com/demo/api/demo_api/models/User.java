package com.demo.api.demo_api.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.demo.api.demo_api.helper.validatorCostum.ValidEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Document(collection = "users")
public class User {
  @Id
  private UUID id;

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password is required")
  @Length(min = 8, message = "Password must be at least 8 characters long")
  private String password;

  public enum EnumRole {
    admin,
    member
  }

  @NotNull(message = "Role cannot be null")
  @ValidEnum(enumClass = User.EnumRole.class, message = "Invalid role")
  private String role;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public User() {
    this.id = UUID.randomUUID();
  }

  public static User loginUser(String email, String password) {
    User user = new User();
    user.setEmail(email);
    user.setPassword(password);
    return user;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
