package com.shop.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.entity.Image;
import com.shop.entity.Product;
import com.shop.respository.ImageRepository;
import com.shop.service.ImageService;

@Service
public class ImageServiceImp implements ImageService {

	@Autowired
	private ImageRepository imageRepository;

	@Override
	public void createImage(Image img) {
		imageRepository.save(img);
	}

	@Override
	public void addNew(Image image) {
		imageRepository.saveAndFlush(image);
	}

	@Override
	public void deleteAll(Product product) {
		List<Image> images = imageRepository.findByProduct(product.getId());
		for (Image img : images) {
			imageRepository.delete(img);
		}
	}

}
