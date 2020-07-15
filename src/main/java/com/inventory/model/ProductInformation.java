package com.inventory.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInformation {
	
	@JsonProperty(required = true)
	@NotNull(message = "Required 'id' is not present")
	private String sellerId;
	private Item item;

}
