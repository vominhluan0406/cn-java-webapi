package com.shop.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shop.entity.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

	@Query(value = "SELECT COUNT(*) FROM status s WHERE s.id = ?1", nativeQuery = true)
	int countByID(long id);
}
