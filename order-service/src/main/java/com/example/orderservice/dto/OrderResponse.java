package com.example.orderservice.dto;

import lombok.*;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderResponse {
	private Long id;
	private String orderNumber;
	private List<OrderLineItemDto> orderLineItemListDto;
}
