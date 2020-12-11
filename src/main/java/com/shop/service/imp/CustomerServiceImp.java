package com.shop.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shop.entity.Customer;
import com.shop.entity.UseVoucher;
import com.shop.entity.Voucher;
import com.shop.entity.form.CustomerForm;
import com.shop.respository.CustomerRepository;
import com.shop.respository.UseVoucherRepository;
import com.shop.respository.VoucherRepository;
import com.shop.service.CustomerService;

@Service
public class CustomerServiceImp implements CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	private VoucherRepository voucherRepository;
	@Autowired
	private UseVoucherRepository useVoucherRepository;

	@Override
	public List<CustomerForm> getAllCustomer() {
		List<CustomerForm> customers = new ArrayList<CustomerForm>();
		List<Customer> customerList = customerRepository.findAll();

		customerList.forEach((c) -> {
			customers.add(new CustomerForm(c));
		});

		return customers;
	}

	@Override
	public Optional<Customer> getCustomerById(Long id) {
		Customer customer = customerRepository.findById(id).get();
		return Optional.ofNullable(customer);
	}

	@Override
	public void createCustomer(Customer customer) {
		String password = customer.getPassword();

		BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();

		customer.setPassword(cryptPasswordEncoder.encode(password));
		customer.setVerified(false);

		customerRepository.saveAndFlush(customer);

		// Tặng voucher NEWUSER cho người dùng mới
		Voucher voucher = voucherRepository.findById((long) 1).get();
		UseVoucher useVoucher = new UseVoucher(customer, voucher);

		useVoucherRepository.save(useVoucher);
	}

	@Override
	public void updateCustomer(Customer customer, long id) {
		Customer customerDB = customerRepository.findById(id).get();

		// Update dữ liệu
		customerDB.setAddress(customer.getAddress());
		customerDB.setFirst_name(customer.getFirst_name());
		customerDB.setLast_name(customer.getLast_name());
		customerDB.setPhone_number(customer.getPhone_number());

		customerRepository.saveAndFlush(customerDB);
	}

	@Override
	public int countCustomerByUsername(String username) {
		return customerRepository.countUserName(username);

	}

	@Override
	public CustomerForm getCustomerByUsername(String username) {
		return new CustomerForm(customerRepository.findByUsername(username));
	}

	@Override
	public int countCustomerById(long id) {
		return customerRepository.countUserByID(id);
	}

	@Override
	public boolean checkPassword(long user_id, String password) {
		String passwordDB = customerRepository.findPasswordByUserID(user_id);
		if (passwordDB.equals(password)) {
			return true;
		}
		return false;
	}

	@Override
	public void changePassword(long user_id, String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String passwordEncode = encoder.encode(password);

		Customer customer = customerRepository.findById(user_id).get();
		customer.setPassword(passwordEncode);

		customerRepository.save(customer);
	}

	@Override
	public void verifyEmail(String email) {
		Customer customer = customerRepository.findByUsername(email);
		customer.setVerified(true);
		customerRepository.save(customer);
	}

	@Override
	public void forgetPassword(Map<String, String> data) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Customer customer = customerRepository.findByUsername(data.get("email"));
		String password = encoder.encode(data.get("password"));
		customer.setPassword(password);
		customerRepository.save(customer);
	}

}
