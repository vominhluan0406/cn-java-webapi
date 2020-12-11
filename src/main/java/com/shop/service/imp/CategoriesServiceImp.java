package com.shop.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.entity.Categories;
import com.shop.respository.CategoriesRepository;
import com.shop.service.CategoriesService;

@Service
public class CategoriesServiceImp implements CategoriesService {

	@Autowired
	private CategoriesRepository categoriesRepository;

	@Override
	public Categories getById(long id) {
		return categoriesRepository.findById(id).get();
	}

}
