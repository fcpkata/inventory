package com.inventory.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.http.HttpStatus;

import com.inventory.exception.ValidationError;
import com.inventory.model.Item;
import com.inventory.model.ProductRequest;
import com.inventory.model.ProductResponse;
import com.inventory.repository.SellerIdRepository;

@RunWith(MockitoJUnitRunner.class)
public class AddItemServiceTest {

	private AddItemService	addItemService;
	
	@Mock
	private CatalogService mockCatalogService;

	@Before
	public void setup() {
		
		List<ValidationError> validationError = new ArrayList<>();
		ProductResponse mockProductResponse  = ProductResponse.builder().validationError(validationError).build();
		when(mockCatalogService.checkProductIsPresent(any())).thenReturn(mockProductResponse);
		addItemService = new AddItemService(mockCatalogService, new SellerIdRepository());
	}

	@Test
	public void shouldReturnError_WhenSellarIdIsInvalid() throws FileNotFoundException, IOException {

		ProductRequest productRequest =  ProductRequest.builder().sellerId("Invalid").build();
		ProductResponse response = addItemService.addItem(productRequest);
		assertThat(response.getValidationError().get(0).getMessage()).isEqualTo("invalid seller id");
	}

	@Test
	public void shouldReturnError_WhenUserIsNotASeller() throws FileNotFoundException, IOException {
		
		ProductRequest productRequest =  ProductRequest.builder().sellerId("A1B2C4").build();
		ProductResponse response = addItemService.addItem(productRequest);
		assertThat(response.getValidationError().get(0).getMessage()).isEqualTo("user is not a registered seller");
	}
	
	@Test
	public void shouldReturnSuccess_WhenSellarIdIsValid() throws FileNotFoundException, IOException {
		ProductRequest productRequest =  ProductRequest.builder().sellerId("A1B2C3").build();
		ProductResponse response = addItemService.addItem(productRequest);
		assertThat(response.getValidationError().size()).isEqualTo(0);
	}
	
	@Test
	public void shouldReturn400_whenProductIDIsNotPresent() throws FileNotFoundException, IOException {
		
		Item item = Item.builder().productId("INVALID").build();
		ProductRequest productRequest =  ProductRequest.builder().sellerId("A1B2C4").item(item).build();
		List<ValidationError> validationError = new ArrayList<>();
		validationError.add(new ValidationError(HttpStatus.BAD_REQUEST, "invalid product id"));
		ProductResponse mockResponse = ProductResponse.builder().validationError(validationError).build();
		when(mockCatalogService.checkProductIsPresent(any())).thenReturn(mockResponse);
		ProductResponse response = addItemService.addItem(productRequest);
		assertThat(response.getValidationError().get(0).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getValidationError().get(0).getMessage()).isEqualTo("invalid product id");
		
	}
	
	
	@Test
	public void shouldReturnMultipleErrors_whenProductIDAndSellerIsNotPresent() throws FileNotFoundException, IOException {
		
		Item item = Item.builder().productId("INVALID").build();
		ProductRequest productRequest =  ProductRequest.builder().sellerId("INVALID").item(item).build();
		List<ValidationError> validationError = new ArrayList<>();
		validationError.add(new ValidationError(HttpStatus.BAD_REQUEST, "invalid product id"));
		ProductResponse mockResponse = ProductResponse.builder().validationError(validationError).build();
		when(mockCatalogService.checkProductIsPresent(any())).thenReturn(mockResponse);
		ProductResponse response = addItemService.addItem(productRequest);
		assertThat(response.getValidationError().get(0).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getValidationError().get(0).getMessage()).isEqualTo("invalid product id");
		assertThat(response.getValidationError().get(1).getMessage()).isEqualTo("invalid seller id");
		assertThat(response.getValidationError().get(2).getMessage()).isEqualTo("user is not a registered seller");
		
	}

}
