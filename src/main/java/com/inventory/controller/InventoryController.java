package com.inventory.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.model.InventoryResponse;
import com.inventory.model.ProductInformation;
import com.inventory.repository.ItemRepository;
import com.inventory.service.AddItemService;

@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class InventoryController {

	private ItemRepository itemRepository;
	private AddItemService addItemService;

	@Autowired
	public InventoryController(ItemRepository itemRepository, AddItemService addItemService) {
		this.itemRepository = itemRepository;
		this.addItemService = addItemService;
	}

	@GetMapping(path = "/item/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InventoryResponse> getItems(@PathVariable(value = "itemId") String itemId) {
		List<ProductInformation> productInformations = itemRepository.fetchItemById(itemId);
		InventoryResponse result = InventoryResponse.builder().productInformations(productInformations).build();
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping(path = "/item/{itemId}/{sellerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InventoryResponse> getItemsBySellerId(@PathVariable(value = "itemId") String itemId,
			@PathVariable(value = "sellerId") String sellerId) {
		List<ProductInformation> productInformations = itemRepository.fetchItemByItemAndSellerId(itemId, sellerId);
		productInformations.get(0).getItem().reduceQuantityByOne();
		InventoryResponse result = InventoryResponse.builder().productInformations(productInformations).build();
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/inventory")
	public ResponseEntity<String> addItem(@Valid @NotNull @RequestBody ProductInformation request) {

		addItemService.addItem(request);
		return new ResponseEntity<String>(HttpStatus.OK);

	}
}
