package com.shop.service;

import com.shop.entity.Image;
import com.shop.entity.Product;

public interface ImageService {

	void createImage(Image img);

	void addNew(Image image);

	void deleteAll(Product product);

}
