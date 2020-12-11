package com.shop.entity.form;

import com.shop.entity.Customer;
import com.shop.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerForm {

	private long id;
	private String last_name;
	private String first_name;
	private String username;
	private String address;
	private String phone_number;
	private Role role;
	private int gender;
	private boolean verified;

	public CustomerForm(Customer customer) {
		this.id = customer.getId();
		this.first_name = customer.getFirst_name();
		this.last_name = customer.getLast_name();
		this.username = customer.getUsername();
		this.address = customer.getAddress();
		this.phone_number = customer.getPhone_number();
		this.role = customer.getRole();
		this.gender = customer.getGender();
		this.verified = customer.isVerified();
	}
}
