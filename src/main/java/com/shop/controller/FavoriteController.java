package com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shop.controller.payload.Respone;
import com.shop.controller.regex.CheckController;
import com.shop.dto.FavoriteDTO;
import com.shop.service.CustomerService;
import com.shop.service.FavoriteService;
import com.shop.service.ProductService;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

	@Autowired
	private FavoriteService favoriteService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	CheckController check;

	@GetMapping("")
	public ResponseEntity<?> getByUser(@RequestParam(required = false) String user) {
		if (!check.isLong(user)) {
			return ResponseEntity.status(400).body(new Respone(400, "User ID Sai"));
		}
		if (customerService.countCustomerById(Long.parseLong(user)) == 0) {
			return ResponseEntity.status(404).body(new Respone(404, "User không tồn tại"));
		}
		try {
			return ResponseEntity.ok(favoriteService.getByUser(Long.parseLong(user)));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PostMapping("")
	public ResponseEntity<?> addFavorite(@RequestBody FavoriteDTO favoriteDTO) {
		try {
			favoriteService.addFavorite(favoriteDTO);
			return ResponseEntity.ok(new Respone(200, "Success"));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PutMapping("")
	public ResponseEntity<?> unFavorite(@RequestBody FavoriteDTO favoriteDTO) {
		if (productService.countByProductID(favoriteDTO.getProduct_id()) == 0) {
			return ResponseEntity.status(404).body(new Respone(404, "Không tìm thấy product"));
		}
		if (customerService.countCustomerById(favoriteDTO.getUser_id()) == 0) {
			return ResponseEntity.status(404).body(new Respone(404, "Không tìm thấy user"));
		}
		if (!favoriteService.checkExist(favoriteDTO)) {
			return ResponseEntity.status(404).body(new Respone(404,
					"User " + favoriteDTO.getUser_id() + " chưa thích Product " + favoriteDTO.getProduct_id()));
		}
		try {
			favoriteService.unFavorite(favoriteDTO);
			return ResponseEntity.ok(new Respone(200, "Success"));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}
}
