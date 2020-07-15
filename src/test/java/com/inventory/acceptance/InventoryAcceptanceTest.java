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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.inventory.model.ProductRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class InventoryAcceptanceTest {

	@Autowired
	private MockMvc mockMvc;

	private ProductRequest user;
	
	private static ObjectMapper mapper;
	
	private MockHttpServletRequestBuilder requestBuilder;
	
	private MockRestServiceServer mockRestServiceServer;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Before
	public void setup() throws FileNotFoundException, IOException {
		Gson gson = new Gson();
		ClassPathResource classPathResource = new ClassPathResource("addProduct.json");
		JsonReader reader = new JsonReader(new FileReader(classPathResource.getFile()));
		user = gson.fromJson(reader, ProductRequest.class);
		mapper = new ObjectMapper();
		
		mockRestServiceServer =  MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void shouldReturnIventoryDetailsForValidBookId() throws Exception {

		mockMvc.perform(get("/v1/item/507f191e810c19729de860ea")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.itemId").value("507f191e810c19729de860ea"))
				.andExpect(jsonPath("$.itemName").value("Vaju"))
				.andExpect(jsonPath("$.price").value(100))
				.andExpect(jsonPath("$.shippingPrice").value(50));

	}
	
	@Test
	public void shouldReturnSuccessForAddPrdocutDetails() throws Exception {
		
		mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(containsString("/catalog/v1/product/")))
		.andRespond(withSuccess());
		requestBuilder = post("/v1/item/");
		requestBuilder.content(mapper.writeValueAsString(user));
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());

	}
}
