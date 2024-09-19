package com.demo.api.demo_api.helper.validatorCostum;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.demo.api.demo_api.helper.exception.CustomException;

@Component
public class UUIDValidator {
  public UUID validate(String uuid) {
    if (uuid == null || uuid.isEmpty()) {
      throw new CustomException("UUID is required");
    }
    return UUID.fromString(uuid);
  }
}
