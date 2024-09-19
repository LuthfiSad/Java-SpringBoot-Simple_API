package com.demo.api.demo_api.Controllers;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.demo.api.demo_api.DTO.ProductDTO;
import com.demo.api.demo_api.helper.ApiResponse;
import com.demo.api.demo_api.helper.Pagination;
import com.demo.api.demo_api.helper.exception.CustomException;
import com.demo.api.demo_api.models.Product;
import com.demo.api.demo_api.services.ProductService;

import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping
  public ResponseEntity<ApiResponse> index(
      @RequestParam(name = "page", required = false, defaultValue = "1") int page,
      @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage) {
    try {
      Pagination<Product> product = productService.getProducts(page, perPage);
      return ResponseEntity.ok(new ApiResponse("success", product.getData(), null, product.getMeta()));
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponse("error", null, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> show(@PathVariable("id") String idStr) {
    try {
      return ResponseEntity.ok(new ApiResponse("success", productService.getProductById(idStr)));
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponse("error", null, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  public ResponseEntity<ApiResponse> createProduct(
      @Valid @ModelAttribute ProductDTO productDTO,
      @RequestParam("image") Optional<MultipartFile> image) {

    try {
      Product createdProduct = productService.createProduct(productDTO, image);
      return new ResponseEntity<>(new ApiResponse("success", createdProduct),
          HttpStatus.CREATED);
    } catch (CustomException e) {
      return ResponseEntity.badRequest().body(new ApiResponse("Validation Error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("error", null, "An unexpected error occurred"));
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse> updateProduct(
      @PathVariable("id") String idStr,
      @Valid @ModelAttribute ProductDTO productDTO,
      @RequestParam("image") Optional<MultipartFile> image) {
    try {

      Product updatedProduct = productService.updateProduct(idStr, productDTO, image);
      return new ResponseEntity<>(new ApiResponse("success", updatedProduct),
          HttpStatus.OK);
    } catch (CustomException e) {
      return ResponseEntity.badRequest().body(new ApiResponse("Validation Error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("error", "An unexpected error occurred"));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") String idStr) {
    try {
      productService.deleteProduct(idStr);
      return new ResponseEntity<>(new ApiResponse("success", "Product deleted", null), HttpStatus.OK);
    } catch (CustomException e) {
      return ResponseEntity.badRequest().body(new ApiResponse("Validation Error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("error", "An unexpected error occurred"));
    }
  }

  @GetMapping("/images/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
    try {
      Path file = Paths.get("src/main/resources/static/images/products/").resolve(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG) // Update this to match the actual image type
            .body(resource);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (MalformedURLException | IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

}
