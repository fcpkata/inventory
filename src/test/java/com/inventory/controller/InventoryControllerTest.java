package com.inventory.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.inventory.exception.ValidationError;
import com.inventory.model.Item;
import com.inventory.model.ProductInformation;
import com.inventory.model.ProductRequest;
import com.inventory.model.ProductResponse;
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

	private ProductRequest productRequest;

	@Before
	public void setup() throws FileNotFoundException, IOException {
		Gson gson = new Gson();
		ClassPathResource classPathResource = new ClassPathResource("addProduct.json");
		JsonReader reader = new JsonReader(new FileReader(classPathResource.getFile()));
		productRequest = gson.fromJson(reader, ProductRequest.class);
		when(mockAddItemService.addItem(any())).thenReturn(new ProductResponse());
		inventoryController = new InventoryController(mockItemRepository, mockAddItemService);
	}

	
	@Test
	public void shouldReturnSingleItem() {
		
		List<ProductInformation> items = new ArrayList<ProductInformation>();
		items.add(getSingleItem());
		
		Mockito.when(mockItemRepository.fetchItemById("507f191e810c19729de860ea")).thenReturn(items);
		ResponseEntity<List<ProductInformation>> response = inventoryController.getItems("507f191e810c19729de860ea");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(items);
	}

	@Test
	public void shouldReturnMultipleItems() {
		List<ProductInformation> items = new ArrayList<ProductInformation>();
		items.add(getSingleItem());
		items.addAll(getMultipleItems());
		Mockito.when(mockItemRepository.fetchItemById("507f191e810c19729de860eb")).thenReturn(items);

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
	 

	@Test
	public void shouldReturn200_whenProductIDIsPresent() throws FileNotFoundException, IOException {

		ResponseEntity<ProductResponse> response = inventoryController.addItem(productRequest);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertNull(response.getBody().getValidationError());
	}

	@Test
	public void shouldReturn400_whenProductIDIsNotPresent() throws FileNotFoundException, IOException {
		List<ValidationError> validationError = new ArrayList<>();
		validationError.add(new ValidationError(HttpStatus.BAD_REQUEST, "invalid product id"));
		validationError.add(new ValidationError(HttpStatus.BAD_REQUEST, "invalid seller id"));
		validationError.add(new ValidationError(HttpStatus.BAD_REQUEST, "user is not a registered seller"));

		ProductResponse mockResponse = ProductResponse.builder().validationError(validationError).build();
		when(mockAddItemService.addItem(any())).thenReturn(mockResponse);
		ResponseEntity<ProductResponse> response = inventoryController.addItem(productRequest);
		assertThat(response.getStatusCodeValue()).isEqualTo(400);
		assertThat(response.getBody().getValidationError().get(0).getMessage()).isEqualTo("invalid product id");
		assertThat(response.getBody().getValidationError().get(1).getMessage()).isEqualTo("invalid seller id");
		assertThat(response.getBody().getValidationError().get(2).getMessage()).isEqualTo("user is not a registered seller");
	}
	
}
