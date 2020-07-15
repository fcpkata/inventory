package com.inventory.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationError{
	
	private HttpStatus statusCode;
	private String message;

	@Override
    public String toString() {
        return "ValidationError{" +
                " statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }
}
