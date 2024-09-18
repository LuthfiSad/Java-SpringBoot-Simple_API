package com.demo.api.demo_api.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.demo.api.demo_api.Product.ProductDTO;
import com.demo.api.demo_api.helper.ImageFileHelper;
import com.demo.api.demo_api.helper.Pagination;
import com.demo.api.demo_api.helper.exception.CustomException;
import com.demo.api.demo_api.models.Product;
import com.demo.api.demo_api.repository.ProductRepository;

import io.jsonwebtoken.io.IOException;

@Service
public class ProductService {

  @Autowired
  private ImageFileHelper imageFileHelper;

  @Autowired
  private ProductRepository productRepository;

  public Pagination<Product> getProducts(int page, int perPage) {
    Pageable pageable = PageRequest.of(page - 1, perPage);
    Page<Product> product = productRepository.findAll(pageable);

    int totalPages = product.getTotalPages();
    int totalData = (int) product.getTotalElements();

    Pagination.Meta meta = new Pagination.Meta(perPage, page, totalPages, totalData);

    return new Pagination<>(product.getContent(), meta);
  }

  public Optional<Product> getProductById(UUID id) {
    if (!productRepository.findById(id).isPresent()) {
      throw new CustomException("Product not found");
    }
    return productRepository.findById(id);
  }

  public Product createProduct(ProductDTO productDTO, Optional<MultipartFile> image) {
    try {
      String imagePath = imageFileHelper.uploadImage(image);
      Product product = new Product();
      product.setName(productDTO.getName());
      product.setDescription(productDTO.getDescription());
      product.setPrice(productDTO.getPrice());
      product.setQuantity(productDTO.getQuantity());
      product.setImage(imagePath);
      product.setCreatedAt(LocalDateTime.now());
      product.setUpdatedAt(LocalDateTime.now());
      return productRepository.save(product);
    } catch (IOException e) {
      // Tangkap IOException dan kirim pesan error jika gagal upload
      throw new CustomException("Failed to upload image: " + e.getMessage());
    }
  }

  public Product updateProduct(UUID id, ProductDTO updatedProductDTO, Optional<MultipartFile> image) {
    Optional<Product> existingProduct = productRepository.findById(id);
    if (!existingProduct.isPresent()) {
      throw new CustomException("Product not found");
    }

    try {
      // Hapus gambar lama
      imageFileHelper.deleteImage(existingProduct.get().getImage());

      // Upload gambar baru
      String imagePath = imageFileHelper.uploadImage(image);
      Product updatedProduct = new Product();
      updatedProduct.setName(updatedProductDTO.getName());
      updatedProduct.setDescription(updatedProductDTO.getDescription());
      updatedProduct.setPrice(updatedProductDTO.getPrice());
      updatedProduct.setQuantity(updatedProductDTO.getQuantity());
      updatedProduct.setImage(imagePath);
      updatedProduct.setUpdatedAt(LocalDateTime.now());

      updatedProduct.setId(id); // Set ID produk yang akan di-update
      return productRepository.save(updatedProduct);
    } catch (IOException e) {
      throw new CustomException("Failed to upload image: " + e.getMessage());
    }
  }

  public void deleteProduct(UUID id) {
    Optional<Product> existingProduct = productRepository.findById(id);
    if (!existingProduct.isPresent()) {
      throw new CustomException("Product not found");
    }
    imageFileHelper.deleteImage(existingProduct.get().getImage());
    productRepository.deleteById(id);
  }

}
