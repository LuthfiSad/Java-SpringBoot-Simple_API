package com.demo.api.demo_api.helper.validatorCostum;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.demo.api.demo_api.helper.exception.CustomException;

@Component
public class ImageValidator {
  public MultipartFile validate(Optional<MultipartFile> fileOpt) {
    // Validate if file is empty
    if (fileOpt.isEmpty()) {
      throw new CustomException("Image is required");
    }
    MultipartFile file = fileOpt.get();
    // Validate file type
    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new CustomException("Invalid image format");
    }
    // Validate file size (e.g., max 5MB)
    if (file.getSize() > 5 * 1024 * 1024) {
      throw new CustomException("File size exceeds limit (5MB)");
    }
    return file;
  }
}
