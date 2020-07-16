package com.inventory.model;

import java.util.List;

import com.inventory.exception.ValidationError;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
	
	private List<ValidationError> validationError;

}
