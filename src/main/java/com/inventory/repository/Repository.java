package com.inventory.repository;

import java.util.List;

import com.inventory.model.ProductInformation;

public interface Repository {

	List<ProductInformation> fetchItemById(String itemId);
	
	List<ProductInformation> fetchItemByItemAndSellerId(String itemId, String sellerId);

}
