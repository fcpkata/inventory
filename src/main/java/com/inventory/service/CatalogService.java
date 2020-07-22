package com.inventory.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.inventory.model.Item;
import com.inventory.model.Product;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CatalogService {
	private RestTemplate restTemplate;

	public CatalogService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<String> checkProductIsPresent(Item item) {
		String uri = System.getProperty("catalogService");
		log.info("Calling catalog service to check product details " + uri);
		List<String> errors = new ArrayList<>();
		if(StringUtils.isEmpty(item.getProductId()))
			return createError(errors);

		return callCatalogService(item.getProductId(), uri, errors);
	}

	private List<String> callCatalogService(String productId, String uri, List<String> errors) {
		ResponseEntity<Product> product = restTemplate.getForEntity(uri + productId, Product.class);
		if(product.getStatusCodeValue() == HttpStatus.NOT_FOUND.value()) 
			errors = createError(errors);
		return errors;
	}

	private List<String> createError(List<String> errors) {
		errors.add("invalid product id");
		return errors;
	}

}
