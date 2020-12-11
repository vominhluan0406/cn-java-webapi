package com.shop.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shop.dto.statistic.resutlt.CustomerBuying;
import com.shop.entity.Order;

@Repository
public interface OrderReponsitory extends JpaRepository<Order, Long> {

	@Query(value = "SELECT customer_id as customerId,count(*) as buyingTimes FROM orders o WHERE o.status = 3 group by o.customer_id  limit 10", nativeQuery = true)
	List<CustomerBuying> getTopBuying();

	@Query(value = "SELECT MAX(o.date) FROM orders o WHERE o.customer_id = ?1", nativeQuery = true)
	String findLastBuying(Long customerID);

	@Query(value = "SELECT MIN(o.date) FROM orders o", nativeQuery = true)
	String findMinDate();

	@Query(value = "SELECT COUNT(*) FROM orders o WHERE o.id = ?1", nativeQuery = true)
	int countOrderById(long id);

	@Query(value = "SELECT * FROM orders o WHERE o.customer_id = ?1 AND o.status = 3", nativeQuery = true)
	List<Order> findByUser(long customer_id);

	@Query(value = "SELECT * FROM orders o WHERE o.status = 3 ", nativeQuery = true)
	List<Order> findAllBySuccess();

}
