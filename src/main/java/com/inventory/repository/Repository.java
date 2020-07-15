package com.inventory.repository;

import java.util.List;

import com.inventory.model.Item;

public interface Repository {

	List<Item> fetchItemById(String itemId);

}
