package com.shop.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ProductDTO {

	private String name;
	private BigDecimal price;
	private long category_id;
	private long quantity;
	private long brand_id;
	private String date;
	private List<String> images;
	private DescriptionDTO description;

}
