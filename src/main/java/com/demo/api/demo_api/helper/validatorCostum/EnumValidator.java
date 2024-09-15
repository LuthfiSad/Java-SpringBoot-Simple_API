package com.demo.api.demo_api.helper.validatorCostum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

  private Enum<?>[] enumValues;

  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    this.enumValues = constraintAnnotation.enumClass().getEnumConstants();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true; // Memungkinkan null, tangani dengan anotasi @NotNull di DTO
    }
    for (Enum<?> enumValue : enumValues) {
      if (enumValue.name().equals(value)) {
        return true;
      }
    }
    return false;
  }
}
