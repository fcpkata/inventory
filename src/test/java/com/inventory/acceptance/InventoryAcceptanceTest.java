package com.inventory.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InventoryAcceptanceTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnProductInformationForItemValidProductId() throws Exception {

		mockMvc.perform(get("/v1/item/507f191e810c19729de860ea")).andDo(print()).andExpect(status().isOk())
		        .andExpect(jsonPath("$[0].sellerId").value("ABC1"))
				.andExpect(jsonPath("$[0].item.productId").value("507f191e810c19729de860ea"))
				.andExpect(jsonPath("$[0].item.price").value(100))
				.andExpect(jsonPath("$[0].item.quantity").value("2"))
				.andExpect(jsonPath("$[0].item.shippingPrice").value(50));

	}
	
	@Test
	public void shouldReturnProductInformationForItemWithMultipleSellers() throws Exception {
		
		mockMvc.perform(get("/v1/item/507f191e810c19729de860eb")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].sellerId").value("ABC1"))
				.andExpect(jsonPath("$[0].item.productId").value("507f191e810c19729de860eb"))
				.andExpect(jsonPath("$[0].item.price").value(900))
				.andExpect(jsonPath("$[0].item.quantity").value("2"))
				.andExpect(jsonPath("$[0].item.shippingPrice").value(50))
				.andExpect(jsonPath("$[1].sellerId").value("XYZ"))
				.andExpect(jsonPath("$[1].item.productId").value("507f191e810c19729de860eb"))
				.andExpect(jsonPath("$[1].item.price").value(850))
				.andExpect(jsonPath("$[1].item.quantity").value("2"))
				.andExpect(jsonPath("$[1].item.shippingPrice").value(50));
	}

}
