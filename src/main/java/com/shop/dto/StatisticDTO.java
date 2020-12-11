package com.shop.dto;

import java.util.List;

import com.shop.dto.statistic.CircleStatistic;
import com.shop.dto.statistic.CustomerStatistic;
import com.shop.dto.statistic.ProductStatistic;
import com.shop.dto.statistic.SalesStatistic;
import lombok.Data;

@Data
public class StatisticDTO {

	private SalesStatistic sales; // Doanh thu theo TG
	private List<CustomerStatistic> customers; // Top khách hàng mua nhiều
	private CircleStatistic circle; // Tông mua các category
	private List<ProductStatistic> product; // Top product bán nhiều

}
