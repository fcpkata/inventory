package com.inventory.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.inventory.model.Item;
import com.inventory.repository.ItemRepository;

@RunWith(MockitoJUnitRunner.class)
public class InventoryControllerTest {

	@InjectMocks
	InventoryController inventoryController;

	@Mock
	ItemRepository itemRepository;

	@Test
	public void shouldReturnSingleItem() {
		Item item = Item.builder()
				.itemId("507f191e810c19729de860ea")
				.itemName("Vaju").price(100).shippingPrice(50)
			    .build();
		Mockito.when(itemRepository.fetchItemById("507f191e810c19729de860ea")).thenReturn(item);

		ResponseEntity<Item> response = inventoryController.getItem("507f191e810c19729de860ea");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(item);
	}

}
