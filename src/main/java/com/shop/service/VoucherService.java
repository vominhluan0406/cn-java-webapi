package com.shop.service;

import java.util.List;

import com.shop.dto.UserVoucherDTO;
import com.shop.entity.Voucher;

public interface VoucherService {

	boolean checkVoucher(String voucher);

	boolean checkVoucherofUser(long user_id, String date, String voucher);

	List<Voucher> getAll();

	void create(Voucher voucher);

	List<Voucher> getByUserID(long user_id);

	void useVoucher(UserVoucherDTO data);

	boolean checkExist(long id);

	void deleteVoucher(long id);

	void updateVoucher(long parseLong, Voucher voucher);

}
