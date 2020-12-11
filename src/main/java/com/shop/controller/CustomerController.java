package com.shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
import com.shop.entity.Customer;
import com.shop.entity.form.CustomerForm;
import com.shop.entity.form.ForgetPasswordForm;
import com.shop.security.jwt.JwtTokenProvider;
import com.shop.service.CustomerService;
import com.shop.service.EmailService;

@RestController
@RequestMapping("/user")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private CheckController check;
	@Autowired
	private EmailService emailService;
	@Autowired
	private JwtTokenProvider jwt;

	@GetMapping("")
	public ResponseEntity<?> getUserByUserName(@RequestParam(required = false) String value) {
		try {
			CustomerForm customer = customerService.getCustomerByUsername(value);
			return ResponseEntity.ok(customer);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllUser() {
		List<CustomerForm> customers = customerService.getAllCustomer();
		return ResponseEntity.ok(customers);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable(value = "id") String user_id) {
		try {
			long id = Long.parseLong(user_id);
			if (customerService.countCustomerById(id) != 0) {
				return ResponseEntity.ok(customerService.getCustomerById(id).get());
			}
			return ResponseEntity.ok(new Respone(404, "Không tìm thấy User có id:" + id));
		} catch (NumberFormatException e) {
			return ResponseEntity.ok(new Respone(400, "User ID không hợp lệ"));
		}
	}

	@PostMapping("/add")
	public ResponseEntity<?> createUser(@RequestBody Customer customerIn) {

		if (!check.isEmail(customerIn.getUsername())) {
			return ResponseEntity.status(400).body(new Respone(400, "Username phải là email"));
		}
		if (!check.isNumberPhone(customerIn.getPhone_number()) || customerIn.getPhone_number().length() != 10) {
			return ResponseEntity.status(400)
					.body(new Respone(400, "Số điện thoại không đúng (SDT di động 03,05,08,09 có 10 số)"));
		}
		if (customerIn.getGender() > 1 || customerIn.getGender() < 0) {
			return ResponseEntity.status(400).body(new Respone(400, "Giới tính không hợp lệ"));
		}

		String userName = customerIn.getUsername();
		if (customerService.countCustomerByUsername(userName) != 0) {
			return ResponseEntity.status(400).body(new Respone(400, "Tài khoản đã tồn tại"));
		}
		try {
			customerService.createCustomer(customerIn);
			emailService.sendMailVerify(customerIn.getUsername());
			return ResponseEntity.ok("Create Success " + customerIn.getUsername());
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody Customer customer) {
		if (!check.isNumberPhone(customer.getPhone_number()) || customer.getPhone_number().length() != 10) {
			return ResponseEntity.status(400)
					.body(new Respone(400, "Số điện thoại không đúng (SDT di động 03,05,08,09 có 10 số)"));
		}
		if (customerService.countCustomerById(id) == 0) {
			return ResponseEntity.status(400).body(new Respone(400, "Không có user có id = " + id));
		}
		if (customer.getGender() > 1 || customer.getGender() < 0) {
			return ResponseEntity.status(400).body(new Respone(400, "Giới tính không hợp lệ"));
		}
		try {
			customerService.updateCustomer(customer, id);
			return ResponseEntity.ok("Update success");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@PutMapping("/pw")
	public ResponseEntity<?> changePassword(@RequestParam(value = "id", required = false) String id,
			@RequestBody Map<String, String> password) {
		if (!check.isInteger(id)) {
			return ResponseEntity.status(400).body(new Respone(400, "USER ID không hợp lệ"));
		}
		long user_id = Long.parseLong(id);
		if (customerService.countCustomerById(user_id) == 0) {
			return ResponseEntity.status(404).body(new Respone(404, "Ko có user"));
		}
		if (!password.containsKey("password")) {
			return ResponseEntity.status(400).body(new Respone(400, "Dữ liệu nhập vào sai"));
		}
		try {
			customerService.changePassword(user_id, password.get("password"));
			return ResponseEntity.ok("OK");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> data) {
		try {
			if (!jwt.validateToken(data.get("token")) && !StringUtils.hasText(data.get("token"))) {
				return ResponseEntity.status(400).body(new Respone(400, "Token không hợp lệ"));
			}
			customerService.verifyEmail(jwt.getUserNameFromJWT(data.get("token")));
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PostMapping("/send-fp")
	public ResponseEntity<?> sendMailFP(@RequestBody Map<String, String> email) {
		try {
			if (!email.containsKey("email")) {
				return ResponseEntity.status(400).body(new Respone(400, "Dữ liệu sai"));
			}
			if (customerService.countCustomerByUsername(email.get("email")) == 0) {
				return ResponseEntity.status(404).body(new Respone(404, "Tài khoản không tồn tại"));
			}
			emailService.sendForgetPassword(email.get("email"));
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PostMapping("/forget")
	public ResponseEntity<?> forgetPassword(@RequestBody ForgetPasswordForm data) {
		try {
			if (!jwt.validateToken(data.getToken()) && !StringUtils.hasText(data.getToken())) {
				return ResponseEntity.status(400).body(new Respone(400, "Token không hợp lệ"));
			}
			Map<String, String> dataU = new HashMap<String, String>();
			dataU.put("email", jwt.getUserNameFromJWT(data.getToken()));
			dataU.put("password", data.getPassword());

			customerService.forgetPassword(dataU);
			return ResponseEntity.ok("ok");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}
}
