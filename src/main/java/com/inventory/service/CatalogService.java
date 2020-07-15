package com.inventory.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.inventory.exception.ValidationError;
import com.inventory.model.Item;
import com.inventory.model.Product;
import com.inventory.model.ProductResponse;

@Service
public class CatalogService {
	private RestTemplate restTemplate;

	public CatalogService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	String uri = "http://catalog/catalog/v1/product/";
	public ProductResponse checkProductIsPresent(Item item) {
		ProductResponse addItemResponse = new ProductResponse();
		List<ValidationError> validationError = new ArrayList<>();
		ResponseEntity<Product> product = restTemplate.getForEntity(uri + item.getProductId(), Product.class);
		if(product.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value()) {
			validationError.add(new ValidationError(HttpStatus.BAD_REQUEST, "invalid product id"));
		}
		addItemResponse.setValidationError(validationError);
		return addItemResponse;
	}

}
