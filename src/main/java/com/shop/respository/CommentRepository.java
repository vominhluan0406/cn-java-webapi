package com.shop.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shop.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query(value = "SELECT * FROM comments c WHERE c.product_id = ?1", nativeQuery = true)
	List<Comment> findByProductId(long id);

	@Query(value = "SELECT c.rate FROM comments c WHERE c.product_id = ?1", nativeQuery = true)
	List<Double> findRateByProduct(long id);

}
