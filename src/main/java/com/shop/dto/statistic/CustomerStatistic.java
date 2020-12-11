package com.shop.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerStatistic {

	private String name;
	private int buying_time;
	private String last_buy;

}
