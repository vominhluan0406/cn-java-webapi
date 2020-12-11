package com.shop.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.respository.RoleRepositoy;
import com.shop.service.RoleService;

@Service
public class RoleServiceImp implements RoleService {

	@Autowired
	RoleRepositoy roleRepositoy;
	

}
