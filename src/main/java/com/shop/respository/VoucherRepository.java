package com.shop.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shop.entity.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

	@Query(value = "SELECT COUNT(*) FROM vouchers v WHERE v.code = ?1", nativeQuery = true)
	int countVoucherByCode(String voucher);

	@Query(value = "SELECT * FROM vouchers v WHERE v.code = ?1", nativeQuery = true)
	Voucher findByCode(String voucher);

	@Query(value = "SELECT COUNT(*) FROM vouchers v WHERE v.id = ?1", nativeQuery = true)
	int countByID(long id);

}
