package com.inventory.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
		items.add(Item.builder().itemId("507f191e810c19729de860ea").itemName("Vaju").price(100).shippingPrice(50)
				.sellerId("ABC1").sellerName("ABC").build());
		items.add(Item.builder().itemId("507f191e810c19729de860eb").itemName("John").price(900).shippingPrice(50)
				.sellerId("ABC1").sellerName("ABC").build());
		items.add(Item.builder().itemId("507f191e810c19729de860eb").itemName("John").price(850).shippingPrice(50)
				.sellerId("XYZ1").sellerName("XYZ").build());
	}

	@Override
	public List<Item> fetchItemById(String itemId) {
		return Optional.of(items.stream().filter(item -> item.getItemId().equals(itemId)).collect(Collectors.toList())).orElseThrow(ItemNotFoundException::new);
	}

}
