package com.shop.service.imp;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shop.entity.Customer;
import com.shop.entity.form.CustomUserDetails;
import com.shop.respository.CustomerRepository;

@Service
public class CustomUserDetailServiceImp implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer customer = customerRepository.findByUsername(username);

		if (customer == null) {
			System.out.println("Khong co user");
			throw new UsernameNotFoundException("Không tìm thấy User " + username);
		}
		System.out.println(customer.getUsername());
		return new CustomUserDetails(customer);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		Customer user = customerRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));

		return new CustomUserDetails(user);
	}

}
