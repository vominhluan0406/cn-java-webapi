package com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.controller.payload.Respone;
import com.shop.dto.StatisticDTO;
import com.shop.service.StatisticsService;

@RequestMapping("/statistic")
@RestController
public class StatisticControlller {

	@Autowired
	private StatisticsService statisticsService;

	@GetMapping("")
	@Cacheable(value = "statistic")
	public ResponseEntity<?> getStatistics() {
		try {
			StatisticDTO statistic = statisticsService.getStatistics();
			return ResponseEntity.ok(statistic);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

}
