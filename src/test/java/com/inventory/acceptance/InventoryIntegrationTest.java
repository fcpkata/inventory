package com.inventory.acceptance;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.inventory.model.ProductInformation;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InventoryIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	private static ObjectMapper mapper;
	
	private MockHttpServletRequestBuilder requestBuilder;
	
	private MockRestServiceServer mockRestServiceServer;
	
	@Autowired
	private RestTemplate restTemplate;

	private Object request;
	
	@Before
	public void setup() throws FileNotFoundException, IOException {
		
		mockRestServiceServer =  MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void shouldReturnProductInformationForItemValidProductId() throws Exception {

		mockMvc.perform(get("/v1/item/PD001")).andDo(print()).andExpect(status().isOk())
		        .andExpect(jsonPath("$[0].sellerId").value("A1B2C5"))
				.andExpect(jsonPath("$[0].item.productId").value("PD001"))
				.andExpect(jsonPath("$[0].item.price").value(100))
				.andExpect(jsonPath("$[0].item.quantity").value(2))
				.andExpect(jsonPath("$[0].item.shippingPrice").value(50));

	}
	
	@Test
	public void shouldReturnProductInformationForItemWithMultipleSellers() throws Exception {
		
		mockMvc.perform(get("/v1/item/PD002")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].sellerId").value("A1B2C3"))
				.andExpect(jsonPath("$[0].item.productId").value("PD002"))
				.andExpect(jsonPath("$[0].item.price").value(200))
				.andExpect(jsonPath("$[0].item.quantity").value("2"))
				.andExpect(jsonPath("$[0].item.shippingPrice").value(50))
				.andExpect(jsonPath("$[1].sellerId").value("A1B2C6"))
				.andExpect(jsonPath("$[1].item.productId").value("PD002"))
				.andExpect(jsonPath("$[1].item.price").value(150))
				.andExpect(jsonPath("$[1].item.quantity").value(5))
				.andExpect(jsonPath("$[1].item.shippingPrice").value(50));
	}
	@Ignore
	@Test
	public void shouldReturnSuccessForAddPrdocutDetails() throws Exception {
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(containsString("/catalog/v1/products/")))
		.andRespond(withSuccess());
		requestBuilder = post("/v1/inventory/");
		createRequest("addProduct.json");
		requestBuilder.content(mapper.writeValueAsString(request));
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());

	}
	@Ignore
	@Test
	public void shouldReturnErrorWhenSellerIdIsNotPresent() throws Exception {
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(containsString("/catalog/v1/products/")))
		.andRespond(withSuccess());
		requestBuilder = post("/v1/inventory/");
		createRequest("addProduct_InvalidSellerId.json");
		requestBuilder.content(mapper.writeValueAsString(request));
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isBadRequest());

	}
	
	private void createRequest(String fileName) throws FileNotFoundException, IOException {
		Gson gson = new Gson();
		ClassPathResource classPathResource = new ClassPathResource(fileName);
		JsonReader reader = new JsonReader(new FileReader(classPathResource.getFile()));
		request =  gson.fromJson(reader, ProductInformation.class);
		mapper = new ObjectMapper();
	}

}
