package com.example.orderservice.dto;


import lombok.*;

import java.util.List;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
	private String orderNumber;
	private List<OrderLineItemDto> orderLineItemListDto;
}
