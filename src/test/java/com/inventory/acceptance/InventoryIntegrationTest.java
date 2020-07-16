package com.inventory.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.model.DeleteItem;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InventoryIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnProductInformationForItemValidProductId() throws Exception {

		mockMvc.perform(get("/v1/item/PD001")).andDo(print()).andExpect(status().isOk())
		        .andExpect(jsonPath("$[0].sellerId").value("A1B2C5"))
				.andExpect(jsonPath("$[0].item.productId").value("PD001"))
				.andExpect(jsonPath("$[0].item.price").value(100))
				.andExpect(jsonPath("$[0].item.quantity").value(1))
				.andExpect(jsonPath("$[0].item.shippingPrice").value(50));

	}
	
	@Test
	public void shouldReturnProductInformationForItemWithMultipleSellers() throws Exception {
		
		mockMvc.perform(get("/v1/item/PD002")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].sellerId").value("A1B2C3"))
				.andExpect(jsonPath("$[0].item.productId").value("PD002"))
				.andExpect(jsonPath("$[0].item.price").value(200))
				.andExpect(jsonPath("$[0].item.quantity").value(2))
				.andExpect(jsonPath("$[0].item.shippingPrice").value(50))
				.andExpect(jsonPath("$[1].sellerId").value("A1B2C6"))
				.andExpect(jsonPath("$[1].item.productId").value("PD002"))
				.andExpect(jsonPath("$[1].item.price").value(150))
				.andExpect(jsonPath("$[1].item.quantity").value(5))
				.andExpect(jsonPath("$[1].item.shippingPrice").value(50));
	}
	
	@Test
	public void shouldReturnRemainingProductInformationAfterDeletingItemAfterCheckout() throws Exception {
		
		List<DeleteItem> deleteItemsList = new ArrayList<DeleteItem>();
		deleteItemsList.add(new DeleteItem("PD001", "A1B2C5", 1));
		deleteItemsList.add(new DeleteItem("PD002", "A1B2C3", 1));
		
		String jsonBody = new ObjectMapper().writeValueAsString(deleteItemsList);
		
		MockHttpServletRequestBuilder requestBuilder =  delete("/v1/items");
		requestBuilder.content(jsonBody);
		requestBuilder.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].sellerId").value("A1B2C5"))
				.andExpect(jsonPath("$[0].item.productId").value("PD001"))
				.andExpect(jsonPath("$[0].item.quantity").value(1))
				.andExpect(jsonPath("$[1].sellerId").value("A1B2C3"))
				.andExpect(jsonPath("$[1].item.productId").value("PD002"))
				.andExpect(jsonPath("$[1].item.quantity").value(1));
	}

}
