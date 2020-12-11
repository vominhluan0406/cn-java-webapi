package com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shop.controller.payload.Respone;
import com.shop.service.AmazonClient;

@RestController
@RequestMapping("/upload")
public class UploadS3Controller {

	@Autowired
	private AmazonClient amazonClient;

	@PostMapping("/uploadFile")
	public ResponseEntity<?> uploadFile(@RequestPart(value = "file") MultipartFile[] file) {
		try {
			return ResponseEntity.ok(amazonClient.uploadFile(file));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PostMapping("/delete/{id-img}")
	public ResponseEntity<?> deleteFile(@PathVariable(name = "id-img") long id) {
		try {
			amazonClient.deleteFile(id);
			return ResponseEntity.ok("deleted");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PostMapping("/delete")
	public ResponseEntity<?> deleteFile1(@RequestParam(value = "url") String id) {
		try {
			amazonClient.deleteFile(id);
			return ResponseEntity.ok("deleted");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

}
