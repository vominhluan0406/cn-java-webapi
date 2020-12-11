package com.shop.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {

	private long user_id;
	private List<OrderDetailsDTO> details;
	private BigDecimal discount;
	private String shipping_address;
	private String buying_date;
	private String note;
	private long status;
	private BigDecimal total;
	private String payment_method;

}
