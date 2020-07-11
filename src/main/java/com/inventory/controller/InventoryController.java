package com.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.model.Item;
import com.inventory.repository.ItemRepository;

@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class InventoryController {

	private ItemRepository itemRepository;

	@Autowired
	public InventoryController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@GetMapping("/item/{itemId}")
	public ResponseEntity<Item> getItem(@PathVariable(value = "itemId") String itemId) {
		Item book = itemRepository.fetchItemById(itemId);
		ResponseEntity<Item> response = new ResponseEntity<Item>(book, HttpStatus.OK);
		return response;
	}

}
