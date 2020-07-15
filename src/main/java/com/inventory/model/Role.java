package com.inventory.model;

import java.io.Serializable;

public enum Role implements Serializable{
	SELLER,
	BUYER;
	
	
	public String getRole() {
        return this.name();
    }

}
