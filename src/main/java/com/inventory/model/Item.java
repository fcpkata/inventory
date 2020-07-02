package com.inventory.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {

	private int itemId;
	private int locationId;
	private int available;

	}
