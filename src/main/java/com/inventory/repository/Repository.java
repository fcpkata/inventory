package com.inventory.repository;

import com.inventory.model.Item;

public interface Repository {

	Item fetchItemById(String itemId);

}
