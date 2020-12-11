package com.shop.controller.regex;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class CheckControllerImp implements CheckController {

	@Override
	public boolean isInteger(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			int d = Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isEmail(String email) {
		String regex = "^[a-z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";
		Pattern pattern = Pattern.compile(regex);
		if (!pattern.matches(regex, email)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isNumberPhone(String numberPhone) {
		String regex = "(0[5|3|8|9])([0-9]{8,8})\\b";
		Pattern pattern = Pattern.compile(regex);
		if (!pattern.matches(regex, numberPhone)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isLong(String price) {
		if (price == null) {
			return false;
		}
		try {
			long priceB = Long.parseLong(price);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
