package com.shop.service;

import java.util.List;
import java.util.Optional;

import com.shop.dto.OrderDTO;
import com.shop.entity.Order;

public interface OrderService {

	Optional<Order> getOderById(long id);

	List<Order> getAllOder();

	void createNewOder(OrderDTO oder);

	void upadteOder(long id, OrderDTO oder);

	void updateStatus(long id, int status);

	int countOrderById(long id);

	int countStatusById(long id);

	boolean checkBuying(long customer_id, long product_id);

	List<Order> getByUser(long user);

}
