package com.microservices.productservice.controllers;

import com.microservices.productservice.dto.ProductRequest;
import com.microservices.productservice.dto.ProductResponse;
import com.microservices.productservice.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/createProduct")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping(value = "/products")
    public List<ProductResponse> getAllProducts() {
        return productService.getProducts();
    }

  @GetMapping(value = "/products/{lowerPrice}/{higherPrice}")
  public List<ProductResponse> getProductsByPriceFilter(@PathVariable BigDecimal lowerPrice, @PathVariable BigDecimal higherPrice) {
    return productService.getFilteredProductByPriceBetween(lowerPrice, higherPrice);
  }
    
    @GetMapping(value = "/product/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
      ProductResponse productResponse = productService.getProduct(id);
      if (productResponse != null) {
          return ResponseEntity.ok(productResponse);
      } else {
         return ResponseEntity.notFound().build();
      }
    }
}
