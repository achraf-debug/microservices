package com.example.inventoryservice;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repositories.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class InventoryServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory = Inventory.builder().skuCode("Iphone_13").quantiy(100).build();
			Inventory inventory1 = Inventory.builder().skuCode("Iphone_11").quantiy(0).build();
			
			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}
}
