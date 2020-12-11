package com.shop.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.service.EmailService;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private EmailService email;

	@GetMapping("")
	public ResponseEntity<?> testBody() {
		return null;
	}

	@SuppressWarnings("deprecation")
	@PostMapping("/date")
	public ResponseEntity<?> testDate(@RequestBody String date) {
		try {
			Date date1 = new Date(date);
			new SimpleDateFormat("dd-MM-YYYY").format(date1);
			Calendar cal = Calendar.getInstance(Locale.ENGLISH);
			cal.setTime(date1);
			return ResponseEntity.ok("Week" + cal.get(Calendar.WEEK_OF_YEAR) + "-" + "Month " + cal.get(Calendar.MONTH)
					+ " " + "Year " + cal.get(Calendar.YEAR));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@PostMapping("/send-mail")
	public ResponseEntity sendMail(@RequestBody Map<String, String> data) {
		try {
			email.sendMailVerify(data.get("email"));

			return ResponseEntity.ok("Ok");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
