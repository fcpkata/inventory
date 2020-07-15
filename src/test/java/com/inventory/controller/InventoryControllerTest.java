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
import com.inventory.model.ProductInformation;
import com.inventory.repository.ItemRepository;

@RunWith(MockitoJUnitRunner.class)
public class InventoryControllerTest {

	@InjectMocks
	InventoryController inventoryController;

	@Mock
	ItemRepository itemRepository;

	@Test
	public void shouldReturnSingleItem() {
		
		List<ProductInformation> items = new ArrayList<ProductInformation>();
		items.add(getSingleItem());
		
		Mockito.when(itemRepository.fetchItemById("507f191e810c19729de860ea")).thenReturn(items);
		ResponseEntity<List<ProductInformation>> response = inventoryController.getItems("507f191e810c19729de860ea");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(items);
	}

	@Test
	public void shouldReturnMultipleItems() {
		List<ProductInformation> items = new ArrayList<ProductInformation>();
		items.add(getSingleItem());
		items.addAll(getMultipleItems());
		Mockito.when(itemRepository.fetchItemById("507f191e810c19729de860eb")).thenReturn(items);

		ResponseEntity<List<ProductInformation>> response = inventoryController.getItems("507f191e810c19729de860eb");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(items);
	}

	private ProductInformation getSingleItem() {
		ProductInformation item =ProductInformation.builder().sellerId("ABC1")
				.item(Item.builder().productId("507f191e810c19729de860ea").price(100).quantity(2).shippingPrice(50).build())
				.build();
		return item;
	}

	private List<ProductInformation> getMultipleItems() {
		ProductInformation itemOne = ProductInformation.builder().sellerId("ABC1")
				.item(Item.builder().productId("507f191e810c19729de860eb").price(900).quantity(2).shippingPrice(50).build())
				.build();
		ProductInformation itemTwo = ProductInformation.builder().sellerId("XYZ")
				.item(Item.builder().productId("507f191e810c19729de860eb").price(850).quantity(2).shippingPrice(50).build())
				.build();
		List<ProductInformation> items = new ArrayList<ProductInformation>();
		items.add(itemOne);
		items.add(itemTwo);
		return items;
	}

}
