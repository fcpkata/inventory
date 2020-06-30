package com.inventory.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.inventory.model.Item;

public class InventoryControllerTest {

	@Test
	public void shouldReturnTest() {
		int itemId = 1002000;
		InventoryController controller = new InventoryController();
		ResponseEntity<Item> itemResponseEntity = controller.getItem(itemId);
		Item item = itemResponseEntity.getBody();
		assertEquals(item.getItemId(), itemId);
	}

}
