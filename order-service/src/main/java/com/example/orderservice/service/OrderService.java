package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItem;
import com.example.orderservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final WebClient webClient;
	@Autowired
	private final OrderRepository orderRepository;
	
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		//Create order from orderRequest
		order.setOrderNumber(orderRequest.getOrderNumber());
		order.setOrderLineItemList(orderRequest.getOrderLineItemListDto()
			.stream().map(this::mapFromDto)
			.collect(Collectors.toList()));
		
		List<String> skuCodes = order.getOrderLineItemList().stream().map(OrderLineItem::getSkuCode).toList();
		
		InventoryResponse[] inventoryResponses = webClient.get()
																	.uri("http://localhost:8082/api/inventory", uriBuilder ->
																		uriBuilder.queryParam("skuCode", skuCodes).build())
																	.retrieve()
																	.bodyToMono(InventoryResponse[].class)
																	.block();
		// Call Inventory service and place order if product is in stock
		boolean isInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
		if (isInStock)
			orderRepository.save(order);
		else throw new IllegalArgumentException("Product is not available in stock, please try later");
	}
	
	public List<OrderResponse> getAllOrders() {
		List<Order> orders = orderRepository.findAll();
		
		return orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
	}
	
	private OrderLineItem mapFromDto(OrderLineItemDto orderLineItemDto) {
		
		return OrderLineItem.builder()
			.id(orderLineItemDto.getId())
			.skuCode(orderLineItemDto.getSkuCode())
			.price(orderLineItemDto.getPrice())
			.quantity(orderLineItemDto.getQuantity()).build();
	}
	
	private OrderLineItemDto mapToDto(OrderLineItem orderLineItem) {
		
		return OrderLineItemDto.builder()
			.id(orderLineItem.getId())
			.skuCode(orderLineItem.getSkuCode())
			.price(orderLineItem.getPrice())
			.quantity(orderLineItem.getQuantity()).build();
	}
	
	private OrderResponse mapToOrderResponse(Order order) {
		
		return OrderResponse.builder()
			.id(order.getId())
			.orderNumber(order.getOrderNumber())
			.orderLineItemListDto(order.getOrderLineItemList()
				.stream().map(this::mapToDto).collect(Collectors.toList())).build();
	}
}

