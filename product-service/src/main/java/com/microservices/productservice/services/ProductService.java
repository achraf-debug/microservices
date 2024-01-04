// Copyright (c) preserved 2023
// ----------------------------
// DO NOT MODIFY
// -----------------------------

package com.microservices.productservice.services;

import com.microservices.productservice.dto.ProductRequest;
import com.microservices.productservice.dto.ProductResponse;
import com.microservices.productservice.model.Product;
import com.microservices.productservice.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get list of all products.
     * @return List<ProductResponse>
     */
    public List<ProductResponse> getProducts() {
      List<ProductResponse> productsResponse = new ArrayList<>();
      List<Product> products = productRepository.findAll();

      for (Product product : products) {
        productsResponse.add(ProductResponse.builder()
          .id(product.getId())
          .name(product.getName())
          .description(product.getDescription())
          .price(product.getPrice()).build());
      }

        return productsResponse;
    }

  /**
   * Create a new product
   *
   * @param productRequest  ProductRequest object containing information about the product to create.
   */
  public void createProduct(ProductRequest productRequest) {
      Product product = Product.builder()
        .name(productRequest.getName())
        .description(productRequest.getDescription())
        .price(productRequest.getPrice()).build();
        productRepository.save(product);
        log.info("Product {} is created successfully ", product.getId());
    }

  /**
   * Get product by Id.
   *
   * @param id    the Id of the product to get.
   *
   * @return ProductResponse
   */
  public ProductResponse getProduct(Long id) {
        Optional<Product> p = productRepository.findById(id);
        if (p.isPresent()) {
            Product product = p.get();
            return ProductResponse.builder()
              .id(product.getId())
              .description(product.getDescription())
              .name(product.getName())
              .price(product.getPrice()).build();
        } else {
            return null;
        }
  }

  public List<ProductResponse> getFilteredProductByPriceBetween(BigDecimal lowerPrice, BigDecimal higherPrice) {
    List<ProductResponse> productsResponse = new ArrayList<>();
    List<Product> products = productRepository.findProductByPriceBetween(lowerPrice, higherPrice);

    for (Product product : products) {
      productsResponse.add(ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice()).build());
    }

    return productsResponse;
  }
}
