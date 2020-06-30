package com.inventory.model;

import lombok.Data;

@Data
public class Item {

	private int itemId;
	private int locationId;
	private int available;

	}
