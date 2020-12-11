package com.shop.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.entity.Brand;
import com.shop.respository.BrandRepository;
import com.shop.service.BrandService;

@Service
public class BrandServiceImp implements BrandService {

	@Autowired
	BrandRepository brandRepository;

	@Override
	public Brand getByID(long id) {
		return brandRepository.findById(id).get();
	}

}
