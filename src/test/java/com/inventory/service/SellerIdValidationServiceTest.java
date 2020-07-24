package com.inventory.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.inventory.repository.SellerIdRepository;

public class SellerIdValidationServiceTest {
	
	private SellerIdValidationService SellerIdValidationService;
	
	private List<String> errors;
	
	@Before
	public void setup() {
		SellerIdValidationService = new SellerIdValidationService(new SellerIdRepository());
		errors = new ArrayList<>();
	}
	
	
	@Test
	public void shouldReturnError_WhenSellarIdIsInvalid() throws FileNotFoundException, IOException {

		errors = SellerIdValidationService.validateSellerId(errors, "Invalid");
		assertThat(errors.get(0)).isEqualTo("invalid seller id");
	}

	@Test
	public void shouldReturnError_WhenUserIsNotASeller() throws FileNotFoundException, IOException {

		errors = SellerIdValidationService.validateSellerId(errors, "A1B2C4");
		assertThat(errors.get(0)).isEqualTo("user is not a registered seller");
		
	}
	
	@Test
	public void shouldReturnError_WhenUserIsASeller() throws FileNotFoundException, IOException {

		errors = SellerIdValidationService.validateSellerId(errors, "A1B2C3");
		assertThat(errors.size()).isEqualTo(0);
		
	}

}
