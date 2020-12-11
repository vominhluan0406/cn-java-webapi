package com.shop.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.controller.payload.Respone;
import com.shop.controller.regex.CheckController;
import com.shop.dto.ProductDTO;
import com.shop.entity.Product;
import com.shop.entity.form.QuantityProduct;
import com.shop.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private CheckController check;

	@GetMapping("/{id}")
	public ResponseEntity<?> getProductById(@PathVariable(value = "id") String product_id)
			throws NoSuchElementException {
		try {
			long id = Long.parseLong(product_id);
			if (productService.countByProductID(id) != 0) {
				return ResponseEntity.ok(productService.getProductById(id).get());
			}
			return ResponseEntity.status(404).body(new Respone(404, "Không tìm thấy sản phẩm"));
		} catch (NumberFormatException e) {
			return ResponseEntity.status(400).body(new Respone(400, "Product ID sai"));
		}
	}

	@GetMapping("")
	@Cacheable(value = "products")
	public List<Product> getAllProduct() {
		return productService.getAllProduct();
	}

	@PostMapping("/add")
	@CacheEvict(value = "products", allEntries = true)
	public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDto) {
		if (!check.isInteger(String.valueOf(productDto.getBrand_id()))) {
			return ResponseEntity.status(400).body(new Respone(400, "Sai brand_id"));
		}
		if (!check.isInteger(String.valueOf(productDto.getCategory_id()))) {
			return ResponseEntity.status(400).body(new Respone(400, "Sai category_id"));
		}
		if (!productService.checkProductName(productDto.getName())) {
			return ResponseEntity.status(400).body(new Respone(400, "Đã có sản phẩm " + productDto.getName()));
		}
		if (!check.isLong(String.valueOf(productDto.getPrice()))) {
			return ResponseEntity.status(HttpStatus.CHECKPOINT).body((new Respone(400, "Price sai định dạng")));
		}
		try {
			productService.createProduct(productDto);
			return ResponseEntity.ok(new Respone(200, "Thêm thành công."));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	@CacheEvict(value = "products", allEntries = true)
	public ResponseEntity<?> updateProduct(@PathVariable long id, @RequestBody ProductDTO productDto) {
		if (!check.isInteger(String.valueOf(productDto.getBrand_id()))) {
			return ResponseEntity.status(400).body(new Respone(400, "Sai brand_id"));
		}
		if (!check.isInteger(String.valueOf(productDto.getCategory_id()))) {
			return ResponseEntity.status(400).body(new Respone(400, "Sai category_id"));
		}
		if (!productService.checkProductNameUpdate(id, productDto.getName())) {
			return ResponseEntity.status(400).body(new Respone(102, "Đã có sản phẩm " + productDto.getName()));
		}
		if (!check.isLong(String.valueOf(productDto.getPrice()))) {
			return ResponseEntity.status(400).body(new Respone(101, "Price sai định dạng"));
		}
		try {
			productService.updateProduct(id, productDto);
			return ResponseEntity.ok(new Respone(200, "Thành công"));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, "Có lỗi rồi bạn"));
		}
	}

	@DeleteMapping("/delete/{id}")
	@CacheEvict(value = "products", allEntries = true)
	public ResponseEntity<?> deleteProduct(@PathVariable String id) {
		if (!check.isLong(id)) {
			return ResponseEntity.status(400).body(new Respone(400, "Sai product id (/delete/" + id + " <==)"));
		}
		long product_id = Long.parseLong(id);
		try {
			productService.deleteProduct(product_id);
			return ResponseEntity.ok("Deleted " + id);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}

	@PutMapping("/qt/{id}")
	@CacheEvict(value = { "products" }, allEntries = true)
	public ResponseEntity<?> updateQuantity(@PathVariable String id, @RequestBody QuantityProduct quantity) {
		if (!check.isInteger(id) && check.isInteger(String.valueOf(quantity.getQuantity()))) {
			return ResponseEntity.status(400).body(new Respone(400, "Id hoặc quantity sai định dạng"));
		}
		long id_product = Long.parseLong(id);
		if (productService.countByProductID(id_product) == 0) {
			return ResponseEntity.status(404).body(new Respone(404, "Ko có product " + id));
		}
		try {
			productService.updateQuantity(id_product, quantity.getQuantity());
			return ResponseEntity.ok("OK");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new Respone(400, e.getMessage()));
		}
	}
}
