package com.shop.dto.statistic;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductStatistic {

	private String name;
	private int buying_time;
	private double rate;
	private String image;
	private BigDecimal price;

}
