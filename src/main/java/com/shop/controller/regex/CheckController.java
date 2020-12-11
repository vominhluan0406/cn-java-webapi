package com.shop.controller.regex;

public interface CheckController {
	boolean isInteger(String strNum);

	boolean isEmail(String email);

	boolean isNumberPhone(String numberPhone);

	boolean isLong(String price);
}
