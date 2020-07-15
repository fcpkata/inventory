package com.inventory.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
	
	@NotNull(message = "Required 'id' is not present")
	private String productId;
	private double price;
	private int quantity;
	private int shippingPrice;

}
