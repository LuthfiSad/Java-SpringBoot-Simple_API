package com.demo.api.demo_api.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.demo.api.demo_api.helper.exception.CustomException;
import com.demo.api.demo_api.helper.validatorCostum.ImageValidator;

@Component
public class ImageFileHelper {

  @Autowired
  private ImageValidator imageValidator;

  private static final String BASE_URL = "http://localhost:8080/";
  private static final String DIR_IMAGE = "src/main/resources/static/images/products/";
  private static final String DIR_URL_IMAGE = "products/images/";

  public String uploadImage(Optional<MultipartFile> imageOpt) {
    try {
      // Validate image
      MultipartFile image = imageValidator.validate(imageOpt);

      // Create unique filename
      String originalFilename = image.getOriginalFilename();
      String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;

      // Save the image to the file system
      Path uploadPath = Paths.get(DIR_IMAGE);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      Path filePath = uploadPath.resolve(uniqueFileName);
      Files.copy(image.getInputStream(), filePath);

      // Return the path to be stored in the database (with the domain path)
      return BASE_URL + DIR_URL_IMAGE + uniqueFileName;
    } catch (IOException e) {
      // Wrap IOException with CustomException
      throw new CustomException("Failed to upload image: " + e.getMessage());
    }
  }

  public void deleteImage(String imagePath) {
    if (imagePath != null) {
      String relativePath = imagePath.replace(BASE_URL, "").replace(DIR_URL_IMAGE, DIR_IMAGE);
      File file = new File(relativePath);
      if (file.exists()) {
        if (!file.delete()) {
          throw new CustomException("Failed to delete image");
        }
      }
    }
  }
}
