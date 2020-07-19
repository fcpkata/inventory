package com.inventory.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.inventory.model.Item;
import com.inventory.model.Product;

@Service
public class CatalogService {
	private RestTemplate restTemplate;

	public CatalogService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	String uri = "http://catalog-jx-production.35.224.175.156.nip.io/catalog/v1/product/";
	public List<String> checkProductIsPresent(Item item) {
		List<String> errors = new ArrayList<>();
		ResponseEntity<Product> product = restTemplate.getForEntity(uri + item.getProductId(), Product.class);
		if(product.getStatusCodeValue() == HttpStatus.NOT_FOUND.value()) {
			errors.add("invalid product id");
		}
		return errors;
	}

}
