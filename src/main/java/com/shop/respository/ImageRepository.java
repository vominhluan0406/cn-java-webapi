package com.shop.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shop.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	@Query(value = "SELECT * FROM images i WHERE i.product_id = ?1", nativeQuery = true)
	List<Image> findByProduct(long id);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM images i WHERE i.product_id = ?1",nativeQuery = true)
	void deleteByProductID(long product_id);

}
