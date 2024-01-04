package com.example.orderservice.controller;


import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
	
	@Autowired
	private final OrderService orderService;
	
	@PostMapping(value = "/createorder")
	@ResponseStatus(value = HttpStatus.CREATED)
	public String createOrder(@RequestBody OrderRequest orderRequest) {
		orderService.placeOrder(orderRequest);
		return "Order placed successfully";
	}
	
	@GetMapping(value = "/orders")
	public List<OrderResponse> getAllOrders() {
		return orderService.getAllOrders();
	}
}
