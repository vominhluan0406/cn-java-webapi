package com.shop.dto;

import lombok.Data;

@Data
public class CommentDTO {

	private long customer_id;
	private long product_id;
	private String message;
	private String date;
	private int rate;
	// private boolean bought;
}
