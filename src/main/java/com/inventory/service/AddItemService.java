package com.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory.model.ProductInformation;
import com.inventory.repository.ItemRepository;

@Service
public class AddItemService {

	private CatalogService catalogService;
	private SellerIdValidationService sellerIdValidationService;
	private ItemRepository itemRepository;
	
	

	public AddItemService(CatalogService catalogService, SellerIdValidationService sellerIdValidationService, ItemRepository itemRepository) {
		this.catalogService = catalogService;
		this.sellerIdValidationService = sellerIdValidationService;
		this.itemRepository = itemRepository;
	}

	public String addItem(ProductInformation reqeust) {

		List<String> errors = catalogService.checkProductIsPresent(reqeust.getItem());
		errors = sellerIdValidationService.validateSellerId(errors, reqeust.getSellerId());
		if(errors.size() > 0) {
			StringBuilder sb = new StringBuilder();
			errors.forEach(error -> sb.append(error).append("\n"));
			return sb.toString();
		}
		else
			return itemRepository.saveItemToInventory(reqeust);
	}

}
