package com.inventory.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

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
		items.add(ProductInformation.builder().sellerId("ABC1")
				.item(Item.builder().productId("507f191e810c19729de860ea").price(100).quantity("2").shippingPrice(50).build())
				.build());
		items.add(ProductInformation.builder().sellerId("ABC1")
				.item(Item.builder().productId("507f191e810c19729de860eb").price(900).quantity("2").shippingPrice(50).build())
				.build());
		items.add(ProductInformation.builder().sellerId("XYZ")
				.item(Item.builder().productId("507f191e810c19729de860eb").price(850).quantity("2").shippingPrice(50).build())
				.build());
	}

	@Override
	public List<ProductInformation> fetchItemById(String itemId) {
		return Optional.of(items.stream()
				.filter(item -> item.getItem().getProductId().equals(itemId))
				.collect(Collectors.toList()))
				.orElseThrow(ItemNotFoundException::new);
	}

}
