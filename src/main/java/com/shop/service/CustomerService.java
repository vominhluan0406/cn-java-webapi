package com.shop.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.shop.entity.Customer;
import com.shop.entity.form.CustomerForm;

public interface CustomerService {

	List<CustomerForm> getAllCustomer();

	Optional<Customer> getCustomerById(Long id);

	void createCustomer(Customer customer);

	void updateCustomer(Customer customer, long id);

	int countCustomerByUsername(String username);

	CustomerForm getCustomerByUsername(String username);

	int countCustomerById(long id);

	boolean checkPassword(long id, String encode);

	void changePassword(long user_id, String string);

	void verifyEmail(String string);

	void forgetPassword(Map<String, String> data);

}
