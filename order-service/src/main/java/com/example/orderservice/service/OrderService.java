package com.example.orderservice.service;

import brave.Span;
import brave.Tracer;
import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.event.OrderPlacedEvent;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItem;
import com.example.orderservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final WebClient.Builder webClientBuilder;
	@Autowired
	private final OrderRepository orderRepository;
	private final Tracer tracer;

	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

	public String placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		//Create order from orderRequest
		order.setOrderNumber(orderRequest.getOrderNumber());
		order.setOrderLineItemList(orderRequest.getOrderLineItemListDto()
			.stream().map(this::mapFromDto).toList());
		
		List<String> skuCodes = order.getOrderLineItemList().stream().map(OrderLineItem::getSkuCode).toList();

		Span inventoryServiceLookup = tracer.nextSpan().name("Inventory-service-span");
		tracer.withSpanInScope(inventoryServiceLookup);
		InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
																	.uri("http://inventory-service/api/inventory", uriBuilder ->
																		uriBuilder.queryParam("skuCode", skuCodes).build())
																	.retrieve()
																	.bodyToMono(InventoryResponse[].class)
																	.block();
		// Call Inventory service and place order if product is in stock
		boolean isInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
		if (isInStock) {
			orderRepository.save(order);
			kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
			return "Order placed successfully";
		} else throw new IllegalArgumentException("Product is not available in stock, please try later");
	}
	
	public List<OrderResponse> getAllOrders() {
		List<Order> orders = orderRepository.findAll();
		
		return orders.stream().map(this::mapToOrderResponse).toList();
	}
	
	private OrderLineItem mapFromDto(OrderLineItemDto orderLineItemDto) {
		return OrderLineItem.builder()
			.skuCode(orderLineItemDto.getSkuCode())
			.price(orderLineItemDto.getPrice())
			.quantity(orderLineItemDto.getQuantity()).build();
	}
	
	private OrderLineItemDto mapToDto(OrderLineItem orderLineItem) {
		return OrderLineItemDto.builder()
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

