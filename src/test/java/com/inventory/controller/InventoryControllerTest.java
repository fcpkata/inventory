package com.inventory.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

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
		List<Item> items = new ArrayList<Item>();
		items.add(getSingleItem());
		Mockito.when(itemRepository.fetchItemById("507f191e810c19729de860ea")).thenReturn(items);

		ResponseEntity<List<Item>> response = inventoryController.getItems("507f191e810c19729de860ea");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(items);
	}

	@Test
	public void shouldReturnMultipleItems() {
		List<Item> items = new ArrayList<Item>();
		items.add(getSingleItem());
		items.addAll(getMultipleItems());
		Mockito.when(itemRepository.fetchItemById("507f191e810c19729de860eb")).thenReturn(items);

		ResponseEntity<List<Item>> response = inventoryController.getItems("507f191e810c19729de860eb");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(items);
	}

	private Item getSingleItem() {
		Item item = Item.builder().itemId("507f191e810c19729de860ea").itemName("Vaju").price(100).shippingPrice(50)
				.build();
		return item;
	}

	private List<Item> getMultipleItems() {
		Item itemOne = Item.builder().itemId("507f191e810c19729de860eb").itemName("John").price(900).shippingPrice(50)
				.sellerId("ABC1").sellerName("ABC").build();
		Item itemTwo = Item.builder().itemId("507f191e810c19729de860eb").itemName("John").price(850).shippingPrice(50)
				.sellerId("XYZ1").sellerName("XYZ").build();
		List<Item> items = new ArrayList<Item>();
		items.add(itemOne);
		items.add(itemTwo);
		return items;
	}

}
