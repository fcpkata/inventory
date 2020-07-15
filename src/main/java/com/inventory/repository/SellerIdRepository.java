package com.inventory.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.inventory.model.Role;

@Component
public class SellerIdRepository {
	
	public Map<String, Role> sellerDetails = new HashMap<>();
	
	public SellerIdRepository() {
		this.sellerDetails = getSellerDetails();
	}

	private Map<String, Role> getSellerDetails() {
		sellerDetails.put("A1B2C3", Role.SELLER);
		sellerDetails.put("A1B2C4", Role.BUYER);
		sellerDetails.put("A1B2C5", Role.SELLER);
		sellerDetails.put("A1B2C6", Role.SELLER);
		sellerDetails.put("A1B2C7", Role.BUYER);
		sellerDetails.put("A1B2C8", Role.SELLER);
		
		return sellerDetails;
	}

}
