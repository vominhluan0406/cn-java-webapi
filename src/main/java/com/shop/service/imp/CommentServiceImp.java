package com.shop.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dto.CommentDTO;
import com.shop.entity.Comment;
import com.shop.entity.Customer;
import com.shop.entity.Order;
import com.shop.entity.Product;
import com.shop.respository.CommentRepository;
import com.shop.respository.CustomerRepository;
import com.shop.respository.OrderDetailsRepository;
import com.shop.respository.OrderReponsitory;
import com.shop.respository.ProductRepository;
import com.shop.service.CommentService;

@Service
public class CommentServiceImp implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private OrderReponsitory orderReponsitory;
	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@Override
	public void addNew(CommentDTO commentDto) {
		Customer customer = customerRepository.findById(commentDto.getCustomer_id()).get();
		Product product = productRepository.findById(commentDto.getProduct_id()).get();
		String message = commentDto.getMessage();
		String date = commentDto.getDate();
		boolean bought = true;
		if (!checkBuying(customer.getId(), product.getId())) {
			bought = false;
		}

		// Cập nhật rate sản phẩm
		int rateDto = commentDto.getRate();
		double rateProduct = 0;
		List<Double> rateDB = commentRepository.findRateByProduct(product.getId());
		if (rateDB.size() == 0) {
			rateProduct = rateDto;
		} else {
			for (double rate : rateDB) {
				rateProduct += rate;
			}
			rateProduct = rateProduct / (double) (rateDB.size());
		}
		product.setRating(rateProduct);
		productRepository.saveAndFlush(product);

		Comment comment = new Comment();
		comment.setCustomer(customer);
		comment.setDate(date);
		comment.setMessage(message);
		comment.setProduct(product);
		comment.setRate(rateDto);
		comment.setBought(bought);

		commentRepository.saveAndFlush(comment);
	}

	@Override
	public List<Comment> getAll() {
		return commentRepository.findAll();
	}

	@Override
	public List<Comment> getByProductID(long id) {
		return commentRepository.findByProductId(id);
	}

	@Override
	public void upadteComment(long id, CommentDTO commentDto) {
		Comment commentDB = commentRepository.findById(id).get();

		Customer customer = customerRepository.findById(commentDto.getCustomer_id()).get();
		Product product = productRepository.findById(commentDto.getProduct_id()).get();
		String message = commentDto.getMessage();
		String date = commentDto.getDate();

		// Cập nhật rate sản phẩm
		int rateOld = commentDB.getRate(); // Rate cũ
		int rateDto = commentDto.getRate(); // Rate mới
		double rateProduct = 0;
		List<Double> rateDB = commentRepository.findRateByProduct(product.getId()); // Dsach rate của product
		for (double rate : rateDB) {
			rateProduct += rate;
		}
		rateProduct = rateProduct + rateDto - rateOld;
		rateProduct = rateProduct / (double) (rateDB.size());

		product.setRating(rateProduct);
		productRepository.save(product);

		commentDB.setCustomer(customer);
		commentDB.setDate(date);
		commentDB.setMessage(message);
		commentDB.setProduct(product);
		commentDB.setRate(rateDto);

		commentRepository.saveAndFlush(commentDB);

	}

	private boolean checkBuying(long customer_id, long product_id) {
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
}
