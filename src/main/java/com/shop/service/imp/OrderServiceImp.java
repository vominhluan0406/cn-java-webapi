package com.shop.service.imp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dto.OrderDTO;
import com.shop.dto.OrderDetailsDTO;
import com.shop.entity.Customer;
import com.shop.entity.Order;
import com.shop.entity.OrderDetail;
import com.shop.entity.Product;
import com.shop.entity.Status;
import com.shop.respository.CustomerRepository;
import com.shop.respository.OrderDetailsRepository;
import com.shop.respository.OrderReponsitory;
import com.shop.respository.ProductRepository;
import com.shop.respository.StatusRepository;
import com.shop.service.OrderService;

@Service
public class OrderServiceImp implements OrderService {

	@Autowired
	private OrderReponsitory orderReponsitory;
	@Autowired
	private OrderDetailsRepository orderDetailsRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private StatusRepository statusRepository;
	@Autowired
	private ProductRepository productRepository;

	@Override
	public Optional<Order> getOderById(long id) {
		return orderReponsitory.findById(id);
	}

	@Override
	public List<Order> getAllOder() {
		return orderReponsitory.findAll();
	}

	@Override
	public void upadteOder(long id, OrderDTO orderDto) {
		Status status = statusRepository.findById(orderDto.getStatus()).get();
		String shipping_address = orderDto.getShipping_address();
		String note = orderDto.getNote();
		BigDecimal discount = orderDto.getDiscount();
		BigDecimal total = orderDto.getTotal();
		String payment_method = orderDto.getPayment_method();

		Order order = orderReponsitory.findById(id).get();
		order.setStatus(status);
		order.setTotal(total);
		order.setDiscount(discount);
		order.setNote(note);
		order.setShipping_address(shipping_address);
		order.setPayment_method(payment_method);
		orderReponsitory.saveAndFlush(order);

		// Cập nhật OrderDetails có OrderID = id
		orderDetailsRepository.deleteByOrderId(id);
		List<OrderDetailsDTO> details = orderDto.getDetails();
		for (OrderDetailsDTO detailDto : details) {
			Product product = productRepository.findById(detailDto.getProduct_id()).get();
			OrderDetail detail = new OrderDetail(order, product, detailDto.getQuantity());
			orderDetailsRepository.saveAndFlush(detail);

			if (order.getStatus().getId() == 4) { // Hủy
				product.setStock(product.getStock() + detail.getQuantity());
			} else if (order.getStatus().getId() == 3) { // Thành công
				product.setBuying_times(product.getBuying_times() + detail.getQuantity());
			}
			productRepository.saveAndFlush(product);
		}
	}

	@Override
	public void createNewOder(OrderDTO orderDto) {
		Customer customer = customerRepository.findById(orderDto.getUser_id()).get();
		Status status = statusRepository.findById(orderDto.getStatus()).get();
		String shipping_address = orderDto.getShipping_address();
		String note = orderDto.getNote();
		BigDecimal discount = orderDto.getDiscount();
		BigDecimal total = orderDto.getTotal();
		String date = orderDto.getBuying_date();
		String payment_method = orderDto.getPayment_method();

		// Tạo mới order
		Order order = new Order(total, discount, shipping_address, date, status, note, customer, payment_method);
		orderReponsitory.saveAndFlush(order);

		List<OrderDetailsDTO> details = orderDto.getDetails();
		for (OrderDetailsDTO detailDto : details) {
			Product product = productRepository.findById(detailDto.getProduct_id()).get();
			product.setStock(product.getStock() - detailDto.getQuantity());// Cập nhật số lượng tồn kho
			productRepository.save(product);
			OrderDetail detail = new OrderDetail(order, product, detailDto.getQuantity());
			orderDetailsRepository.saveAndFlush(detail);
		}
	}

	@Override
	public void updateStatus(long id, int status_id) {
		Order order = orderReponsitory.findById(id).get();
		Status status = statusRepository.findById((long) status_id).get();

		Set<OrderDetail> details = order.getDetails();
		for (OrderDetail detail : details) {
			Product product = productRepository.findById(detail.getProduct().getId()).get();
			if (status_id == 4) { // Hủy
				product.setStock(product.getStock() + detail.getQuantity());
			} else if (status_id == 3) { // Thành công
				product.setBuying_times(product.getBuying_times() + detail.getQuantity());
			}
			productRepository.saveAndFlush(product);
		}

		order.setStatus(status);
		orderReponsitory.saveAndFlush(order);
	}

	@Override
	public int countOrderById(long id) {
		return orderReponsitory.countOrderById(id);
	}

	@Override
	public int countStatusById(long id) {

		return statusRepository.countByID(id);
	}

	@Override
	public boolean checkBuying(long customer_id, long product_id) {
		List<Order> orders = orderReponsitory.findByUser(customer_id);
		if (orders.size() == 0) {
			return false;
		}
		for (Order order : orders) {
			if (orderDetailsRepository.checkProduc(order.getId(), product_id) != 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Order> getByUser(long user_id) {
		return orderReponsitory.findByUser(user_id);
	}

}
