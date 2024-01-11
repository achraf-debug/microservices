package com.example.orderservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemDto {
	private String skuCode;
	private BigDecimal price;
	private Integer quantity;
}
