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
	public void shouldReturnIventoryDetailsForValidBookId() throws Exception {

		mockMvc.perform(get("/v1/item/507f191e810c19729de860ea")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.itemId").value("507f191e810c19729de860ea"))
				.andExpect(jsonPath("$.itemName").value("Vaju"))
				.andExpect(jsonPath("$.price").value(100))
				.andExpect(jsonPath("$.shippingPrice").value(50));

	}

}
