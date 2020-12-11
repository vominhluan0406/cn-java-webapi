package com.shop.controller.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Respone {
	private int status_code;
	private String message;
}
