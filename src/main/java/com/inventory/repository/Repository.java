package com.inventory.repository;

import java.util.List;
import java.util.Optional;

import com.inventory.model.DeleteItem;
import com.inventory.model.ProductInformation;

public interface Repository {

	List<ProductInformation> fetchItemById(String itemId);

	List<ProductInformation> deleteItems(List<DeleteItem> deleteItemsList);

	Optional<ProductInformation> fetchItemBySellerIdAndId(String itemId, String sellerId);

}
