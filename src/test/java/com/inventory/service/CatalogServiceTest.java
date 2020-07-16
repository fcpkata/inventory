package com.inventory.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.inventory.model.Item;
import com.inventory.model.Product;
import com.inventory.model.ProductResponse;

@RunWith(MockitoJUnitRunner.class)
public class CatalogServiceTest {
	
	private static final String url = "http://catalog-jx-production.35.224.175.156.nip.io/catalog/v1/product/";

	@Mock
	private RestTemplate mockRestTemplate;
	
	private CatalogService service;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private Item item;
	
	@Before
	public void setUp() {
		service = new CatalogService(mockRestTemplate) ;
		item = Item.builder().productId("PD001").build();
	}
	@Test
	public void shouldReturnSuccess_WithValidProductId() {
		ResponseEntity<Product> mockResponse = new ResponseEntity<Product>(new Product(), HttpStatus.OK);
		when(mockRestTemplate.getForEntity(eq(url+"PD001"), eq(Product.class))).thenReturn(mockResponse);
		ProductResponse response = service.checkProductIsPresent(item);
		assertThat(response.getValidationError().size()).isEqualTo(0);
	}
	
	@Test
	public void shouldReturnException_withInvalidProductId() {
		ResponseEntity<Product> mockResponse = new ResponseEntity<Product>(new Product(), HttpStatus.NOT_FOUND);
		when(mockRestTemplate.getForEntity(eq(url+"PD001"), eq(Product.class))).thenReturn(mockResponse);
		ProductResponse response = service.checkProductIsPresent(item);
		assertThat(response.getValidationError().get(0).getMessage()).isEqualTo("invalid product id");
		assertThat(response.getValidationError().get(0).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

}
