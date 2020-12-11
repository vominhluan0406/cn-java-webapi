package com.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shop.controller.payload.Respone;
import com.shop.controller.regex.CheckController;
import com.shop.dto.CommentDTO;
import com.shop.entity.Comment;
import com.shop.service.CommentService;
import com.shop.service.CustomerService;
import com.shop.service.ProductService;

@RestController
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CheckController check;

	@PostMapping("/add")
	@CacheEvict(value = { "comment", "products" }, allEntries = true)
	public ResponseEntity<?> addComment(@RequestBody CommentDTO comment) {
		if (checkRequest(comment).getStatusCode().value() != 200) {
			return checkRequest(comment);
		}
		try {
			commentService.addNew(comment);
			return ResponseEntity.ok("Thêm comment thành công");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, "Thêm comment thất bại"));
		}
	}

	@GetMapping("")
	@Cacheable(value = "comment")
	public ResponseEntity<?> getAll() {
		List<Comment> comments = commentService.getAll();
		return ResponseEntity.ok(comments);
	}

	@GetMapping("/search")
	public ResponseEntity<?> getByProduct(@RequestParam(value = "id", required = false) String product_id) {
		if (!check.isInteger(product_id)) {
			return ResponseEntity.status(400).body(new Respone(400, "Sai product_id"));
		}
		long id = Long.parseLong(product_id);
		List<Comment> comments = commentService.getByProductID(id);
		if (comments.size() != 0) {
			return ResponseEntity.ok(comments);
		}
		return ResponseEntity.status(404).body(new Respone(404, "Không tìm thấy comment."));
	}

	@PutMapping("/{id}")
	@CacheEvict(value = { "comment", "products" }, allEntries = true)
	public ResponseEntity<?> upadteComment(@RequestBody CommentDTO comment,
			@PathVariable(value = "id") String commentID) {
		try {
			if (!check.isLong(commentID)) {
				return ResponseEntity.status(400).body(new Respone(400, "Sai ID"));
			}
			if (checkRequest(comment).getStatusCode().value() != 200) {
				return checkRequest(comment);
			}
			long id = Long.parseLong(commentID);
			commentService.upadteComment(id, comment);
			return ResponseEntity.ok(new Respone(200, "Update Success"));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

//	@GetMapping("/buy")
//	public ResponseEntity<?> checkUserBuy(@RequestParam(value = "user", required = false) String user_id,
//			@RequestParam(value = "product", required = false) String product_id) {
//		try {
//			if (!check.isLong(user_id) || !check.isLong(product_id)) {
//				return ResponseEntity.status(400).body(new Respone(400, "User ID hoặc Product ID sai"));
//			}
//			if (!orderService.checkBuying(Long.parseLong(user_id), Long.parseLong(product_id))) {
//				return ResponseEntity.ok(0);// Chưa mua sp
//			}
//			return ResponseEntity.ok(1);// Đã mua sp
//		} catch (Exception e) {
//			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
//		}
//	}

	private ResponseEntity<?> checkRequest(CommentDTO comment) {
		if (!check.isLong(String.valueOf(comment.getCustomer_id()))) {
			return ResponseEntity.status(400).body(new Respone(400, "Customer ID sai định dạng"));
		}
		if (!check.isLong(String.valueOf(comment.getProduct_id()))) {
			return ResponseEntity.status(400).body(new Respone(400, "Product ID sai định dạng"));
		}
		if (!check.isInteger(String.valueOf(comment.getProduct_id()))) {
			return ResponseEntity.status(400).body(new Respone(400, "Rate sai định dạng"));
		}
		if (customerService.countCustomerById(comment.getCustomer_id()) == 0) {
			return ResponseEntity.status(400).body(new Respone(400, "Không có user có id " + comment.getCustomer_id()));
		}
		if (productService.countByProductID(comment.getProduct_id()) == 0) {
			return ResponseEntity.status(404)
					.body(new Respone(404, "Không có Product có id " + comment.getProduct_id()));
		}
		return ResponseEntity.ok("");
	}

}
