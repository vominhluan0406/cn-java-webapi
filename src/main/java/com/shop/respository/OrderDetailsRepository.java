package com.shop.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shop.entity.OrderDetail;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Integer> {

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM order_details od WHERE od.order_id = ?1", nativeQuery = true)
	void deleteByOrderId(long id);

	@Query(value = "SELECT * FROM order_details o WHERE o.order_id = ?1", nativeQuery = true)
	List<OrderDetail> findByOderId(long id);

	@Query(value = "SELECT EXISTS(SELECT * FROM order_details o WHERE o.order_id = ?1 AND o.product_id = ?2)", nativeQuery = true)
	int checkProduc(long order_id, long product_id);

	@Query(value = "SELECT COUNT(*) FROM order_details o WHERE o.product_id = ?1", nativeQuery = true)
	int countProduct(long product_id);

}
