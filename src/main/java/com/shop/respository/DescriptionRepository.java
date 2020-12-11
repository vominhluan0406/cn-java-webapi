package com.shop.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shop.entity.DescriptionProduct;

@Repository
public interface DescriptionRepository extends JpaRepository<DescriptionProduct, Long> {

	@Query(value = "SELECT * FROM descriptions d WHERE d.product_id = ?1", nativeQuery = true)
	DescriptionProduct findByProduct(long id);

}
