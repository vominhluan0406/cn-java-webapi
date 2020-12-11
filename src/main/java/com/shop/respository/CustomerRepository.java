package com.shop.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shop.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query(value = "SELECT * FROM users u WHERE u.username = ?1	", nativeQuery = true)
	Customer findByUsername(String username);

	@Query(value = "SELECT COUNT(*) FROM users u WHERE u.username = ?1", nativeQuery = true)
	int countUserName(String username);

	@Query(value = "SELECT COUNT(*) FROM users u WHERE u.id = ?1", nativeQuery = true)
	int countUserByID(long id);

	@Query(value = "SELECT u.password FROM users u WHERE u.id = ?1", nativeQuery = true)
	String findPasswordByUserID(long user_id);
//
//	@Query(value = "", nativeQuery = true)
//	List<Customer> findByBuyingTime();

}
