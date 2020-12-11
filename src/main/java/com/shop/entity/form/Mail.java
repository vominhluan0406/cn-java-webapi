package com.shop.entity.form;

import java.util.Map;

import lombok.Data;

@Data
public class Mail {

	private String from;
	private String mailTo;
	private String subject;
	private Map<String, Object> props;

}
