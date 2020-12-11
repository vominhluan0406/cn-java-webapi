package com.shop.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dto.UserVoucherDTO;
import com.shop.entity.Customer;
import com.shop.entity.UseVoucher;
import com.shop.entity.Voucher;
import com.shop.respository.CustomerRepository;
import com.shop.respository.UseVoucherRepository;
import com.shop.respository.VoucherRepository;
import com.shop.service.VoucherService;

@Service
public class VoucherServiceImp implements VoucherService {

	@Autowired
	private VoucherRepository voucherRepository;
	@Autowired
	private UseVoucherRepository useVoucherRepository;
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public boolean checkVoucher(String voucher) {
		if (voucherRepository.countVoucherByCode(voucher) == 0) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean checkVoucherofUser(long user_id, String dateString, String voucher) {
		voucher = voucher.toUpperCase();
		Voucher voucherDB = voucherRepository.findByCode(voucher);

		if (useVoucherRepository.countByUserVoucher(user_id, voucherDB.getId()) == 0) {
			return false;
		}

		Date date_voucher = new Date(dateString);
		Date start_voucher = new Date(voucherDB.getStart());
		Date end_voucher = new Date(voucherDB.getEnd());

		if (date_voucher.before(start_voucher) || date_voucher.after(end_voucher)) {
			return false;
		}

		return true;
	}

	@Override
	public List<Voucher> getAll() {
		return voucherRepository.findAll();
	}

	@Override
	public void create(Voucher voucher) {
		Voucher voucher_new = new Voucher();
		List<Customer> customers = customerRepository.findAll();
		List<UseVoucher> useVouchers = new ArrayList<UseVoucher>();

		//
		for (Customer customer : customers) {
			UseVoucher useVoucher = new UseVoucher(customer, voucher_new);
			useVouchers.add(useVoucher);
		}
		useVoucherRepository.saveAll(useVouchers);

		voucher_new.setDiscount_percent(voucher.getDiscount_percent());
		voucher_new.setEnd(voucher.getEnd());
		voucher_new.setStart(voucher.getStart());
		voucher_new.setName(voucher.getName());
		voucher_new.setVoucher(voucher.getVoucher().toUpperCase());
		voucherRepository.save(voucher_new);
	}

	@Override
	public List<Voucher> getByUserID(long user_id) {
		List<Long> id_voucher = useVoucherRepository.findByUserID(user_id);
		List<Voucher> vouchers = new ArrayList<Voucher>();
		for (long id : id_voucher) {
			Voucher vo = voucherRepository.findById(id).get();
			vouchers.add(vo);
		}
		return vouchers;
	}

	@Override
	public void useVoucher(UserVoucherDTO data) {
		Voucher voucher = voucherRepository.findByCode(data.getVoucher());
		useVoucherRepository.deleteByUser(data.getUser_id(), voucher.getId());
	}

	@Override
	public boolean checkExist(long id) {
		if (voucherRepository.countByID(id) == 0) {
			return false;
		}
		return true;
	}

	@Override
	public void deleteVoucher(long id) {
		voucherRepository.deleteById(id);
	}

	@Override
	public void updateVoucher(long id, Voucher voucher) {
		Voucher voucherDB = voucherRepository.findById(id).get();

		voucherDB.setDiscount_percent(voucher.getDiscount_percent());
		voucherDB.setEnd(voucher.getEnd());
		voucherDB.setStart(voucher.getStart());
		voucherDB.setName(voucher.getName());
		voucherDB.setVoucher(voucher.getVoucher());

		voucherRepository.saveAndFlush(voucherDB);
	}

}
