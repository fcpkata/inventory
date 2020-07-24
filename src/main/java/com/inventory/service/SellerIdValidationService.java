package com.inventory.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inventory.model.Role;
import com.inventory.repository.SellerIdRepository;

@Component
public class SellerIdValidationService {

	private SellerIdRepository sellerIdRepository;

	public SellerIdValidationService(SellerIdRepository sellerIdRepository) {
		this.sellerIdRepository = sellerIdRepository;
	}

	public List<String> validateSellerId(List<String> response, String id) {

		if(checkSellerIdIsPresent(id)) 
			return checkSellerRole(response, id);

		else 
			return createErrorResponse(response, "invalid seller id");
	}

	private boolean checkSellerIdIsPresent(String id) {
		return sellerIdRepository.sellerDetails.entrySet().stream()
				.anyMatch(sellerId -> sellerId.getKey().equals(id));
	}

	private List<String> checkSellerRole(List<String> response, String id) {

		boolean isPresent =  sellerIdRepository.sellerDetails.entrySet().stream()
				.filter(sellerId -> sellerId.getKey().equals(id))
				.anyMatch(sellerId -> sellerId.getValue().equals(Role.SELLER));

		if(isPresent) 
			return response;

		else 
			return createErrorResponse(response, "user is not a registered seller");
	}

	private List<String> createErrorResponse(List<String> response , String errorMessage) {
		response.add(errorMessage);
		return response;
	}


}
