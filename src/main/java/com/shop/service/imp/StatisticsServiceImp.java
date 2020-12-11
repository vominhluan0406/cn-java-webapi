package com.shop.service.imp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dto.StatisticDTO;
import com.shop.dto.statistic.CircleStatistic;
import com.shop.dto.statistic.CustomerStatistic;
import com.shop.dto.statistic.ProductStatistic;
import com.shop.dto.statistic.SalesStatistic;
import com.shop.dto.statistic.resutlt.CustomerBuying;
import com.shop.entity.Customer;
import com.shop.entity.Order;
import com.shop.entity.Product;
import com.shop.respository.CustomerRepository;
import com.shop.respository.OrderReponsitory;
import com.shop.respository.ProductRepository;
import com.shop.service.StatisticsService;

@Service
public class StatisticsServiceImp implements StatisticsService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderReponsitory orderReponsitory;
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public StatisticDTO getStatistics() throws Exception {
		StatisticDTO statistics = new StatisticDTO();

		// ProductStatistics
		List<Product> productsDB = productRepository.getTopByBuyingTime();
		List<ProductStatistic> productStat = new ArrayList<ProductStatistic>();
		for (Product p : productsDB) {
			ProductStatistic product = new ProductStatistic();
			String image = p.getImages().get(0).getUrl();
			BigDecimal price = p.getPrice();
			double rate = p.getRating();
			product.setBuying_time((int) p.getBuying_times());
			product.setName(p.getName());
			product.setImage(image);
			product.setRate(rate);
			product.setPrice(price);
			productStat.add(product);
		}

		// CircleStatistic
		long smartphoneBuy = productRepository.countBuyingTimeByCategoryID(1) == null ? 0
				: productRepository.countBuyingTimeByCategoryID(1);
		long laptopBuy = productRepository.countBuyingTimeByCategoryID(2) == null ? 0
				: productRepository.countBuyingTimeByCategoryID(2);
		long tabletBuy = productRepository.countBuyingTimeByCategoryID(3) == null ? 0
				: productRepository.countBuyingTimeByCategoryID(3);
		long accessoriesBuy = productRepository.countBuyingTimeByCategoryID(4) == null ? 0
				: productRepository.countBuyingTimeByCategoryID(4);
		CircleStatistic circleStatistic = new CircleStatistic(smartphoneBuy, laptopBuy, tabletBuy, accessoriesBuy);

		// CustomerStatistic
		List<CustomerBuying> customerBuying = orderReponsitory.getTopBuying();
		List<CustomerStatistic> customerStatistics = new ArrayList<>();
		for (CustomerBuying c : customerBuying) {
			Customer customer = customerRepository.findById(c.getCustomerID()).get();
			String name = customer.getFirst_name() + " " + customer.getLast_name();
			int buying_time = c.getBuyingTimes();
			String last_buy = orderReponsitory.findLastBuying(c.getCustomerID());
			CustomerStatistic customerStatistic = new CustomerStatistic(name, buying_time, last_buy);
			customerStatistics.add(customerStatistic);
		}

		// SaleStatistic
		SalesStatistic salesStatistic = new SalesStatistic();
		String minDate = orderReponsitory.findMinDate();
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(new Date(minDate));
		Calendar nowDate = Calendar.getInstance();
		nowDate.setTime(new Date());

		List<Order> orderS = orderReponsitory.findAllBySuccess();
		orderS.sort((a, b) -> {
			if (new Date(a.getDate()).after(new Date(b.getDate()))) {
				return 1;
			}
			return 0;
		});
		for (Order o : orderS) {
			System.out.println(o.getDate());
		}
		Map<String, BigDecimal> weekStatistic = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> monthStatistic = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> yearStatistic = new HashMap<String, BigDecimal>();

		System.out.println(orderS.size());
		if (orderS.size() != 0) {
			for (Order o : orderS) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date(o.getDate()));
				System.out.println(calendar);
				String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
				String year = String.valueOf(calendar.get(Calendar.YEAR));
				String week = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
				if (!monthStatistic.containsKey(month + "-" + year)) {
					monthStatistic.put(month + "-" + year, o.getTotal());
				} else {
					BigDecimal tmpTotal = monthStatistic.get(month + "-" + year);
					monthStatistic.put(month + "-" + year, tmpTotal.add(o.getTotal()));
				}
				if (!yearStatistic.containsKey(year)) {
					yearStatistic.put(year, o.getTotal());
				} else {
					BigDecimal tmpTotal = yearStatistic.get(year);
					yearStatistic.put(year, tmpTotal.add(o.getTotal()));
				}
				if (!weekStatistic.containsKey(week + "-" + year)) {
					weekStatistic.put(week + "-" + year, o.getTotal());
				} else {
					BigDecimal tmpTotal = weekStatistic.get(week + "-" + year);
					weekStatistic.put(week + "-" + year, tmpTotal.add(o.getTotal()));
				}
			}
		}

		salesStatistic.setMonth(monthStatistic);
		salesStatistic.setWeek(weekStatistic);
		salesStatistic.setYear(yearStatistic);

		// StatisticDTO
		statistics.setProduct(productStat);
		statistics.setCircle(circleStatistic);
		statistics.setSales(salesStatistic);
		statistics.setCustomers(customerStatistics);

		return statistics;
	}

}
