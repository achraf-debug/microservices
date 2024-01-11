package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
public class InventoryService {
	
	@Autowired
	private final InventoryRepository inventoryRepository;
	
	// Verification for only one product
//	public Optional<Inventory> isInStock(String skuCode) {
//		return inventoryRepository.findBySkuCode(skuCode);
//	}
	
	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCodes) {
		return inventoryRepository.findBySkuCodeIn(skuCodes).stream().map(inventory -> InventoryResponse.builder()
			.skuCode(inventory.getSkuCode())
			.isInStock(inventory.getQuantiy() > 0).build()).toList();
	}
}
