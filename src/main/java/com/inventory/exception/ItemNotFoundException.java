package com.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class ItemNotFoundException extends HttpClientErrorException{
	private static final long serialVersionUID = 1L;

	public ItemNotFoundException() {
		super(HttpStatus.NOT_FOUND, "Invalid item Id");
	}

}
