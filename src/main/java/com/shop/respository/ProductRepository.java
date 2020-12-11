package com.shop.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shop.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query(value = "SELECT * FROM products u WHERE u.id = ?1", nativeQuery = true)
	Product getByID(long id);

	@Query(value = "SELECT COUNT(*) FROM products p WHERE p.name = ?1", nativeQuery = true)
	int countProductName(String name);

	@Query(value = "SELECT COUNT(*) FROM products p WHERE p.id = ?1", nativeQuery = true)
	int countProductByID(long id);

	@Query(value = "SELECT * FROM products p order by p.buying_times desc limit 10", nativeQuery = true)
	List<Product> getTopByBuyingTime();

	@Query(value = "SELECT SUM(p.buying_times) from products p WHERE p.category = ?1 AND p.buying_times > 0", nativeQuery = true)
	Long countBuyingTimeByCategoryID(long id);
}
