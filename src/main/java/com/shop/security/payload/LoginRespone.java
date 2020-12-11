package com.shop.security.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRespone {

	private String access_token;

	private String token_type = "Bearer";


	public LoginRespone(String token) {
		this.access_token = token;
	}

}
