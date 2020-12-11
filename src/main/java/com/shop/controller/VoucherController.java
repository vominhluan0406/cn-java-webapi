package com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.shop.dto.UserVoucherDTO;
import com.shop.entity.Voucher;
import com.shop.service.CustomerService;
import com.shop.service.VoucherService;

@RestController
@RequestMapping("/voucher")
public class VoucherController {

	@Autowired
	private VoucherService voucherService;
	@Autowired
	private CheckController check;
	@Autowired
	private CustomerService customerService;

	@PostMapping("/check")
	public ResponseEntity<?> checkVoucherofUser(@RequestBody UserVoucherDTO userVoucherDTO) {
		try {
			if (!voucherService.checkVoucher(userVoucherDTO.getVoucher())) {
				return ResponseEntity.status(400).body(new Respone(400, "Voucher không tồn tại"));
			}
			if (!voucherService.checkVoucherofUser(userVoucherDTO.getUser_id(), userVoucherDTO.getDate(),
					userVoucherDTO.getVoucher())) {
				return ResponseEntity.ok(false);
			}
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@GetMapping("")
	public ResponseEntity<?> getAll() {
		try {
			return ResponseEntity.ok(voucherService.getAll());
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PostMapping("")
	public ResponseEntity<?> createVoucher(@RequestBody Voucher voucher) {
		try {
			voucherService.create(voucher);
			return ResponseEntity.ok("OK");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@GetMapping("/user")
	public ResponseEntity<?> getByUser(@RequestParam(value = "id", required = false) String id) {
		try {
			if (!check.isLong(id)) {
				return ResponseEntity.status(400).body(new Respone(400, "Sai user id"));
			}
			long user_id = Long.parseLong(id);
			if (customerService.countCustomerById(user_id) == 0) {
				return ResponseEntity.status(400).body(new Respone(400, "Không có User"));
			}
			return ResponseEntity.ok(voucherService.getByUserID(user_id));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PostMapping("/use")
	public ResponseEntity<?> useVoucher(@RequestBody UserVoucherDTO data) {
		try {
			voucherService.useVoucher(data);
			return ResponseEntity.ok("OK");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteVoucher(@PathVariable String id) {
		try {
			if (!check.isLong(id)) {
				return ResponseEntity.status(400).body(new Respone(400, "Sai định dạng ID"));
			}
			if (!voucherService.checkExist(Long.parseLong(id))) {
				return ResponseEntity.status(400).body(new Respone(400, "Voucher không tồn tại"));
			}
			voucherService.deleteVoucher(Long.parseLong(id));
			return ResponseEntity.ok("OK");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateVoucher(@PathVariable String id, @RequestBody Voucher voucher) {
		try {
			if (!check.isLong(id)) {
				return ResponseEntity.status(400).body(new Respone(400, "Sai định dạng ID"));
			}
			if (!voucherService.checkExist(Long.parseLong(id))) {
				return ResponseEntity.status(400).body(new Respone(400, "Voucher không tồn tại"));
			}
			voucherService.updateVoucher(Long.parseLong(id), voucher);
			return ResponseEntity.ok("OK");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}
}
