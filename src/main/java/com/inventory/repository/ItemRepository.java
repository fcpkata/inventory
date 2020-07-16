package com.inventory.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.inventory.exception.ItemNotFoundException;
import com.inventory.model.DeleteItem;
import com.inventory.model.Item;
import com.inventory.model.ProductInformation;

@Component
public class ItemRepository implements Repository {

	private List<ProductInformation> items;
	private List<ProductInformation> finalItems;

	public ItemRepository() {
		init();
	}

	private void init() {
		this.items = new ArrayList<>();
		items.add(ProductInformation.builder().sellerId("A1B2C5")
				.item(Item.builder().productId("PD001").price(100).quantity(2).shippingPrice(50).build())
				.build());
		items.add(ProductInformation.builder().sellerId("A1B2C3")
				.item(Item.builder().productId("PD002").price(200).quantity(2).shippingPrice(50).build())
				.build());
		
		items.add(ProductInformation.builder().sellerId("A1B2C6")
				.item(Item.builder().productId("PD002").price(150).quantity(5).shippingPrice(50).build())
				.build());

		items.add(ProductInformation.builder().sellerId("A1B2C2")
				.item(Item.builder().productId("PD001").price(1200).shippingPrice(1150).quantity(2).build())
				.build());
	}

	@Override
	public List<ProductInformation> fetchItemById(String itemId) {
		return Optional.of(items.stream()
				.filter(item -> item.getItem().getProductId().equals(itemId))
				.collect(Collectors.toList()))
				.orElseThrow(ItemNotFoundException::new);
	}
	
	@Override
	public Optional<ProductInformation> fetchItemBySellerIdAndId(String itemId, String sellerId) {
		return Optional.of(items.stream()
				.filter(item -> item.getItem().getProductId().equals(itemId) && item.getSellerId().equalsIgnoreCase(sellerId)).findFirst())
				.orElseThrow(ItemNotFoundException::new);
	}
	
	@Override
	public List<ProductInformation> deleteItems(List<DeleteItem> deleteItemsList) {
		this.finalItems = new ArrayList<>();
		
		for(DeleteItem deleteItem : deleteItemsList) {
			Optional<ProductInformation> deleteQuantityOfItemById = fetchItemBySellerIdAndId(deleteItem.getItemId(), deleteItem.getSellerId());
			if (deleteQuantityOfItemById.isPresent()) {
				int quantity = deleteQuantityOfItemById.get().getItem().getQuantity() - deleteItem.getQuatity();
				deleteQuantityOfItemById.get().getItem().setQuantity(quantity);
				finalItems.add(deleteQuantityOfItemById.get());
			}
		}
		
		return finalItems;
	}

}
