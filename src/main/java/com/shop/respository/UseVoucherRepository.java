package com.shop.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.shop.entity.UseVoucher;

public interface UseVoucherRepository extends JpaRepository<UseVoucher, Long> {

	@Query(value = "SELECT EXISTS(SELECT * FROM use_voucher u WHERE u.customer_id = ?1 AND u.voucher_id = ?2)", nativeQuery = true)
	boolean checkUser(long user_id, long voucher_id);

	@Query(value = "SELECT v.voucher_id FROM use_voucher v WHERE v.customer_id = ?1 AND v.used = false", nativeQuery = true)
	List<Long> findByUserID(long user_id);

	@Query(value = "SELECT COUNT(*) FROM use_voucher v WHERE v.customer_id = ?1 AND v.used = false AND v.voucher_id = ?2", nativeQuery = true)
	int countByUserVoucher(long user_id, long voucher);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM use_voucher v WHERE v.customer_id = ?1 AND v.voucher_id = ?2", nativeQuery = true)
	void deleteByUser(long user_id, long id);

}
