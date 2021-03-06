package com.inventory.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.inventory.exception.ItemNotFoundException;
import com.inventory.model.Item;
import com.inventory.model.ProductInformation;

@Component
public class ItemRepository implements Repository {

	private List<ProductInformation> items;

	public ItemRepository() {
		init();
	}

	private void init() {
		this.items = new ArrayList<>();
		items.add(ProductInformation.builder().sellerId("A1B2C5")
				.item(Item.builder().productId("PD001").price(100).quantity(2).build())
				.build());
		items.add(ProductInformation.builder().sellerId("A1B2C3")
				.item(Item.builder().productId("PD002").price(200).quantity(2).build())
				.build());
		items.add(ProductInformation.builder().sellerId("A1B2C6")
				.item(Item.builder().productId("PD002").price(150).quantity(5).build())
				.build());
	}

	@Override
	public List<ProductInformation> fetchItemById(String itemId) {
		return Optional.of(items.stream()
				.filter(item -> item.getItem().getProductId().equals(itemId))
				.collect(Collectors.toList()))
				.orElseThrow(ItemNotFoundException::new);
	}

	public void saveItemToInventory(ProductInformation reqeust) {
		try {
			items.add(reqeust);
		} catch (Exception e) {
			throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY, "unable to add product to inventory");
		}
		
		
	}

}