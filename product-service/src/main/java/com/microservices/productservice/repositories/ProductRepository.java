package com.microservices.productservice.repositories;

import com.microservices.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, Long> {

	public List<Product> findProductByPriceBetween(BigDecimal valeur1, BigDecimal valeur2);
}
