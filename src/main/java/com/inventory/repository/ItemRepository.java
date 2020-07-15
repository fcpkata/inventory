package com.inventory.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.inventory.exception.ItemNotFoundException;
import com.inventory.model.Item;

@Component
public class ItemRepository implements Repository {

	private List<Item> items;

	public ItemRepository() {
		init();
	}

	private void init() {
		this.items = new ArrayList<>();
		items.add(Item.builder().productId("507f191e810c19729de860ea").productName("Vaju").price(100).shippingPrice(50)
				.build());
	}

	@Override
	public Item fetchItemById(String itemId) {
		return items.stream().filter(item -> item.getProductId().equals(itemId)).findFirst()
				.orElseThrow(ItemNotFoundException::new);
	}	

}
