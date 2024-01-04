package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.service.InventoryService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/inventory")
@Builder
@Data
@RequiredArgsConstructor
public class InventoryController {
	
	@Autowired
	private final InventoryService inventoryService;
	
	
	// for just one product to verify !!
	
//	@GetMapping(value = "/{sku_code}")
//	@ResponseStatus(HttpStatus.OK)
//	public boolean isInStock(@PathVariable String sku_code) {
//		Optional<Inventory> inventory = inventoryService.isInStock(sku_code);
//		if (inventory.isPresent()) return inventory.get().getQuantiy() > 0; else
//			return false;
//	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
		return inventoryService.isInStock(skuCode);
	}
}
