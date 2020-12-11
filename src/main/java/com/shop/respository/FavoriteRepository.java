package com.shop.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shop.entity.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	@Query(value = "SELECT COUNT(*) FROM favorites f WHERE f.customer_id = ?1 AND f.product_id = ?2", nativeQuery = true)
	int countByUserAndProduct(long user_id, long product_id);

	@Query(value = "SELECT * FROM favorites f WHERE f.customer_id = ?1 AND f.product_id = ?2", nativeQuery = true)
	Favorite findByUserAndProduct(long user_id, long product_id);

	@Query(value = "SELECT * FROM favorites f WHERE f.customer_id = ?1", nativeQuery = true)
	List<Favorite> findByUser(long user_id);

}
