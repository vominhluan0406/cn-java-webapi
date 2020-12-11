package com.shop.security.payload;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class LoginRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

}
