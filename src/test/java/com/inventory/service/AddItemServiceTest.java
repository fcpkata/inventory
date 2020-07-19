package com.inventory.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.inventory.model.Item;
import com.inventory.model.ProductInformation;
import com.inventory.repository.ItemRepository;

@RunWith(MockitoJUnitRunner.class)
public class AddItemServiceTest {

	private AddItemService	addItemService;
	
	@Mock
	private CatalogService mockCatalogService;
	
	@Mock
	private SellerIdValidationService mockSellerIdValidationService;

	@Mock
	private ItemRepository mockItemRepository;

	@Before
	public void setup() {
		
		List<String> errors = new ArrayList<>();
		when(mockCatalogService.checkProductIsPresent(any())).thenReturn(errors);
		when(mockSellerIdValidationService.validateSellerId(any(), any())).thenReturn(errors);
		addItemService = new AddItemService(mockCatalogService, mockSellerIdValidationService, mockItemRepository);
	}

	@Test
	public void shouldReturnSuccess_WithValidInformation() throws FileNotFoundException, IOException {
		ProductInformation request =  ProductInformation.builder().sellerId("A1B2C3").build();
		String response = addItemService.addItem(request);
		assertNull(response);
	}
	
	@Test
	public void shouldReturn400_whenProductIDIsNotPresent() throws FileNotFoundException, IOException {
		
		mockErrorResponseFromCatalogService();
		mockErrorResponseFromSellerIdValidationService();
		Item item = Item.builder().productId("INVALID").build();
		ProductInformation request = ProductInformation.builder().sellerId("A1B2C4").item(item).build();
		String response = addItemService.addItem(request);
		assertThat(response).isEqualTo("invalid product id\nuser is not a registered seller\n");
		
	}

	private void mockErrorResponseFromSellerIdValidationService() {
		List<String> errors = new ArrayList<>();
		errors.add("invalid product id");
		errors.add("user is not a registered seller");
		when(mockSellerIdValidationService.validateSellerId(any(), any())).thenReturn(errors);
		
	}

	private void mockErrorResponseFromCatalogService() {
		List<String> errors = new ArrayList<>();
		errors.add("invalid product id");
		when(mockCatalogService.checkProductIsPresent(any())).thenReturn(errors);
	}

}
