package com.shop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shop.controller.payload.Respone;
import com.shop.controller.regex.CheckController;
import com.shop.dto.OrderDTO;
import com.shop.dto.OrderDetailsDTO;
import com.shop.entity.Order;
import com.shop.service.CustomerService;
import com.shop.service.OrderService;
import com.shop.service.ProductService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private CheckController check;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductService productService;

	@GetMapping("")
	@Cacheable(value = "order")
	public List<Order> getAllOder() {
		return orderService.getAllOder();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOderById(@PathVariable(name = "id") String order_id) {
		if (!check.isLong(order_id)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Respone(100, "Order id sai"));
		}
		long id = Long.parseLong(order_id);
		if (orderService.getOderById(id) != null) {
			return ResponseEntity.ok(orderService.getOderById(id));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Respone(100, "Không tìm thấy"));
	}

	@PostMapping("/add")
	@CacheEvict(value = { "statistic", "order", "products" }, allEntries = true)
	public ResponseEntity<?> addOrder(@RequestBody OrderDTO order) {
		ResponseEntity<?> res = checkOrder(order);
		if (res.getStatusCode().value() != 200) {
			return res;
		}
		try {
			orderService.createNewOder(order);
			return ResponseEntity.ok("OK");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PutMapping("/{id}")
	@CacheEvict(value = { "statistic", "order", "products" }, allEntries = true)
	public ResponseEntity<?> updateOder(@PathVariable long id, @RequestBody OrderDTO order) {
		ResponseEntity<?> res = checkOrder(order);
		if (res.getStatusCode().value() != 200) {
			return res;
		}
		try {
			orderService.upadteOder(id, order);
			return ResponseEntity.ok("Update " + id);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PutMapping("/status/{id}")
	@CacheEvict(value = { "statistic", "order", "products" }, allEntries = true)
	public ResponseEntity<?> updateStatus(@PathVariable(value = "id") String order_id,
			@RequestBody Map<String, Integer> status) {
		if (!check.isLong(order_id) || !check.isInteger(String.valueOf(status.get("status_id")))) {
			return ResponseEntity.status(400).body(new Respone(400, "Sai order id hoặc status"));
		}
		long id = Long.parseLong(order_id);
		int status_id = status.get("status_id");
		if (orderService.countOrderById(id) == 0) {
			return ResponseEntity.status(404).body(new Respone(404, "Không có order"));
		}
		if (orderService.countStatusById(status_id) == 0) {
			return ResponseEntity.status(404).body(new Respone(404, "Không có trạng thái có id " + status_id));
		}
		try {
			orderService.updateStatus(id, status_id);
			return ResponseEntity.status(200).body("OK");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@GetMapping("/search")
	@JsonIgnoreProperties({ "customer" })
	public ResponseEntity<?> getByUserID(@RequestParam(value = "user", required = false) String user_id) {
		try {
			if (!check.isLong(user_id)) {
				return ResponseEntity.status(400).body(new Respone(400, "User ID hoặc Product ID sai"));
			}
			long user = Long.parseLong(user_id);
			return ResponseEntity.ok(orderService.getByUser(user));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	private ResponseEntity<?> checkOrder(OrderDTO order) {
		if (!check.isLong(String.valueOf(order.getUser_id()))) {
			return ResponseEntity.status(400).body(new Respone(100, "User ID sai định dạng"));
		}
		if (customerService.countCustomerById(order.getUser_id()) == 0) {
			return ResponseEntity.status(400).body(new Respone(101, "Không có User có ID " + order.getUser_id()));
		}
		List<OrderDetailsDTO> details = order.getDetails();
		for (OrderDetailsDTO detail : details) {
			if (productService.countByProductID(detail.getProduct_id()) == 0) {
				return ResponseEntity.status(404)
						.body(new Respone(404, "Không có Product có id " + detail.getProduct_id()));
			}
		}

		return ResponseEntity.ok(new Respone(200, "OK"));
	}
}
