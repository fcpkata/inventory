package com.inventory.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.inventory.exception.ValidationError;
import com.inventory.model.ProductRequest;
import com.inventory.model.ProductResponse;
import com.inventory.model.Role;
import com.inventory.repository.SellerIdRepository;

@Service
public class AddItemService {
	
	private CatalogService catalogService;
	private SellerIdRepository sellerIdRepository;
	
	public AddItemService(CatalogService catalogService, SellerIdRepository sellerIdRepository) {
		this.catalogService = catalogService;
		this.sellerIdRepository = sellerIdRepository;
	}

	public ProductResponse addItem(ProductRequest user) {

		ProductResponse response = catalogService.checkProductIsPresent(user.getItem());
		response = validateSellerId(response, user.getSellerId());
		response = checkSeller(response, user.getSellerId());
		
		return response ;
	}
	
	private ProductResponse checkSeller(ProductResponse response, String id) {
		boolean value = sellerIdRepository.sellerDetails.entrySet().stream()
				.filter(sellerId -> sellerId.getKey().equals(id))
				.anyMatch(sellerId -> sellerId.getValue().equals(Role.SELLER));
		if(!value)
			response.getValidationError().add(new ValidationError(HttpStatus.BAD_REQUEST, "user is not a registered seller"));
		return response;
	}

	private ProductResponse validateSellerId(ProductResponse response, String id) {

		boolean value =  sellerIdRepository.sellerDetails.entrySet().stream()
				.anyMatch(sellerId -> sellerId.getKey().equals(id));
		if(!value)
			response.getValidationError().add(new ValidationError(HttpStatus.BAD_REQUEST, "invalid seller id"));
		return response;
	}

	
}
