package com.shop.service;

import java.util.List;
import java.util.Optional;

import com.shop.dto.ProductDTO;
import com.shop.entity.Product;

public interface ProductService {
	Optional<Product> getProductById(Long id);

	List<Product> getAllProduct();

	void createProduct(ProductDTO productDto);

	void deleteProduct(Long id);

	void updateProduct(Long id, ProductDTO productDto);

	boolean checkProductName(String name);

	boolean checkProductNameUpdate(long id, String name);

	int countByProductID(long id);

	void updateQuantity(long id_product, int quantity);
}
