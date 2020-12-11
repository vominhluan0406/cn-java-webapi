package com.shop.dto.statistic;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

@Data
public class SalesStatistic {

	private Map<String, BigDecimal> week;
	private Map<String, BigDecimal> month;
	private Map<String, BigDecimal> year;

}
