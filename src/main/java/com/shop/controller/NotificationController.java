package com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shop.controller.payload.Respone;
import com.shop.controller.regex.CheckController;
import com.shop.dto.NotificationDTO;
import com.shop.service.NotificationService;

@RestController
@RequestMapping("/noti")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;
	@Autowired
	private CheckController check;

	@PostMapping("/add")
	public ResponseEntity<?> createNew(@RequestBody NotificationDTO notificationDTO) {
		try {
			notificationService.createNew(notificationDTO);
			return ResponseEntity.ok("OK");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@GetMapping("")
	public ResponseEntity<?> getAll() {
		try {
			return ResponseEntity.ok(notificationService.getAllNotSeen());
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@DeleteMapping("/clear")
	public ResponseEntity<?> clearAll(@RequestParam(value = "id", required = false) String id) {
		try {
			if (check.isLong(id)) {
				notificationService.deleteByID(Long.parseLong(id));
				return ResponseEntity.ok("Success");
			}
			notificationService.clearAll();
			return ResponseEntity.ok("Success");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

}
