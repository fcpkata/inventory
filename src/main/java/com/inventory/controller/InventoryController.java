package com.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.model.DeleteItem;
import com.inventory.model.ProductInformation;
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
	public ResponseEntity<List<ProductInformation>> getItems(@PathVariable(value = "itemId") String itemId) {
		List<ProductInformation> book = itemRepository.fetchItemById(itemId);
		ResponseEntity<List<ProductInformation>> response = new ResponseEntity<List<ProductInformation>>(book, HttpStatus.OK);
		return response;
	}
	
	@DeleteMapping("/items")
	public ResponseEntity<List<ProductInformation>> deleteItems(@RequestBody List<DeleteItem> deleteItemsList) {
		List<ProductInformation> deleteItems = itemRepository.deleteItems(deleteItemsList);
		
		ResponseEntity<List<ProductInformation>> response = new ResponseEntity<List<ProductInformation>>(deleteItems, HttpStatus.OK);
		return response;
	}

}
