package com.inventory.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.inventory.model.Item;
import com.inventory.model.ProductInformation;
import com.inventory.repository.ItemRepository;
import com.inventory.service.AddItemService;
import com.inventory.service.CatalogService;

@RunWith(MockitoJUnitRunner.class)
public class InventoryControllerTest {

	private InventoryController inventoryController;

	@Mock
	ItemRepository mockItemRepository;

	@Mock
	private CatalogService mockCatalogService;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private AddItemService mockAddItemService;

	private ProductInformation request;

	@Before
	public void setup() throws FileNotFoundException, IOException {
		Gson gson = new Gson();
		ClassPathResource classPathResource = new ClassPathResource("addProduct.json");
		JsonReader reader = new JsonReader(new FileReader(classPathResource.getFile()));
		request = gson.fromJson(reader, ProductInformation.class);
		doNothing().when(mockAddItemService).addItem(any());
		inventoryController = new InventoryController(mockItemRepository, mockAddItemService);
	}

	
	@Test
	public void shouldReturnSingleItem() {
		
		List<ProductInformation> items = new ArrayList<ProductInformation>();
		items.add(getSingleItem());
		
		when(mockItemRepository.fetchItemById("507f191e810c19729de860ea")).thenReturn(items);
		ResponseEntity<List<ProductInformation>> response = inventoryController.addInventory("507f191e810c19729de860ea");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(items);
	}

	@Test
	public void shouldReturnMultipleItems() {
		List<ProductInformation> items = new ArrayList<ProductInformation>();
		items.add(getSingleItem());
		items.addAll(getMultipleItems());
		when(mockItemRepository.fetchItemById("507f191e810c19729de860eb")).thenReturn(items);

		ResponseEntity<List<ProductInformation>> response = inventoryController.addInventory("507f191e810c19729de860eb");

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
	 

	@Test
	public void shouldReturn200_whenProductIDIsPresent() throws FileNotFoundException, IOException {

		ResponseEntity<String> response = inventoryController.addItem(request);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	public void shouldReturn400_whenProductIDIsNotPresent() throws FileNotFoundException, IOException {
		expectedException.expect(HttpClientErrorException.class);
		expectedException.expectMessage("invalid product id\nuser is not a registered seller\n");
		doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "invalid product id\nuser is not a registered seller\n")).when(mockAddItemService).addItem(any());
		inventoryController.addItem(request);
	}
	
}
