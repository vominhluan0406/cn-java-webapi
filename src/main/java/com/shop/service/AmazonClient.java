package com.shop.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonClient {
	List<String> uploadFile(MultipartFile[] multipartFile);

	void deleteFile(long id);

	void deleteFile(String url);
}
