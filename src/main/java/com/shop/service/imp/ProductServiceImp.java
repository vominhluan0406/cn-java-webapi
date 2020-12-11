package com.shop.service.imp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dto.DescriptionDTO;
import com.shop.dto.ProductDTO;
import com.shop.entity.Brand;
import com.shop.entity.Categories;
import com.shop.entity.DescriptionProduct;
import com.shop.entity.Image;
import com.shop.entity.Product;
import com.shop.respository.BrandRepository;
import com.shop.respository.CategoriesRepository;
import com.shop.respository.DescriptionRepository;
import com.shop.respository.ImageRepository;
import com.shop.respository.OrderDetailsRepository;
import com.shop.respository.OrderReponsitory;
import com.shop.respository.ProductRepository;
import com.shop.service.ProductService;

@Service
public class ProductServiceImp implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private BrandRepository brandRepository;
	@Autowired
	private CategoriesRepository categoriesRepository;
	@Autowired
	private DescriptionRepository descriptionRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private OrderReponsitory orderReponsitory;
	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@Override
	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	@Override
	public List<Product> getAllProduct() {
		return (ArrayList<Product>) productRepository.findAll();
	}

	@Override
	public void createProduct(ProductDTO productDto) {

		// Lấy thông tin product từ productDto
		Brand brand = brandRepository.findById(productDto.getBrand_id()).get();
		Categories category = categoriesRepository.findById(productDto.getCategory_id()).get();
		String name = productDto.getName();
		BigDecimal price = productDto.getPrice();
		String date = productDto.getDate();
		long stock = productDto.getQuantity();
		DescriptionDTO descriptionDTO = productDto.getDescription();

		// lưu product
		Product product = new Product(name, price, date, brand, category, stock);
		product.setBuying_times(0);
		product.setRating(0);
		productRepository.saveAndFlush(product);

		// lưu description
		DescriptionProduct description = new DescriptionProduct(descriptionDTO, product);
		descriptionRepository.save(description);

		// lưu danh sách image
		List<String> images = productDto.getImages();
		for (String img : images) {
			Image image = new Image(product, img);
			imageRepository.saveAndFlush(image);
		}
	}

	@Override
	public void deleteProduct(Long id) {
		if (orderDetailsRepository.countProduct(id) != 0) {
			
		}
		productRepository.deleteById(id);
	}

	@Override
	public void updateProduct(Long id, ProductDTO productDto) {

		// Lấy product cũ
		Product product = productRepository.findById(id).get();

		Brand brand = brandRepository.findById(productDto.getBrand_id()).get();
		Categories category = categoriesRepository.findById(productDto.getCategory_id()).get();
		String name = productDto.getName();
		BigDecimal price = productDto.getPrice();
		String date = productDto.getDate();
		long stock = productDto.getQuantity();

		product.setBrand(brand);
		product.setCategory(category);
		product.setPrice(price);
		product.setStock(stock);
		product.setDate_arrive(date);
		product.setName(name);
		productRepository.saveAndFlush(product);

		DescriptionDTO descriptionDTO = productDto.getDescription();
		DescriptionProduct description = descriptionRepository.findByProduct(id);
		description.dtoTo(descriptionDTO, product);
		descriptionRepository.saveAndFlush(description);

		List<String> images = productDto.getImages();

//		List<Image> imageList = imageRepository.findByProduct(id);
//		for (Image img : imageList) {
//			imageRepository.delete(img);
//		}

		imageRepository.deleteByProductID(id);
		for (String img : images) {
			Image image = new Image(product, img);
			imageRepository.saveAndFlush(image);
		}

	}

	@Override
	public boolean checkProductName(String name) {
		if (productRepository.countProductName(name) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkProductNameUpdate(long id, String name) {
		String product_name = productRepository.findById(id).get().getName();
		if (!product_name.equals(name)) {
			if (productRepository.countProductName(name) == 0) {
				return true;
			}
			return false;
		} else {
			if (productRepository.countProductName(name) == 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int countByProductID(long id) {
		return productRepository.countProductByID(id);
	}

	@Override
	public void updateQuantity(long id, int quantity) {
		Product product = productRepository.findById(id).get();
		product.setStock(quantity);
		productRepository.saveAndFlush(product);
	}
}
