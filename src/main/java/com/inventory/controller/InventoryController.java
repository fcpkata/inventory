package com.inventory.controller;

import java.util.Optional;

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

import com.inventory.model.ProductResponse;
import com.inventory.model.Item;
import com.inventory.model.ProductRequest;
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

	@GetMapping("/item/{itemId}")
	public ResponseEntity<Item> getItem(@PathVariable(value = "itemId") String itemId) {
		Item book = itemRepository.fetchItemById(itemId);
		ResponseEntity<Item> response = new ResponseEntity<Item>(book, HttpStatus.OK);
		return response;
	}

	@PostMapping("/item")
	public ResponseEntity<ProductResponse> addItem(@NotNull @RequestBody ProductRequest user) {
		
		ProductResponse response = addItemService.addItem(user);
		return Optional.ofNullable(response.getValidationError()).filter(errors -> errors.size() > 0)
		.map(value -> new ResponseEntity<ProductResponse>(response, HttpStatus.BAD_REQUEST))
		.orElse(new ResponseEntity<ProductResponse>(response,HttpStatus.OK));
		
	}
}
