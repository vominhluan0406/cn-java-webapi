package com.shop.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.entity.DescriptionProduct;
import com.shop.entity.Product;
import com.shop.respository.DescriptionRepository;
import com.shop.service.DescriptionService;

@Service
public class DescriptionServiceImp implements DescriptionService {

	@Autowired
	private DescriptionRepository descriptionRepository;

	@Override
	public void addNew(DescriptionProduct description) {
		descriptionRepository.saveAndFlush(description);
	}

	@Override
	public void updateDescription(DescriptionProduct description, Product product) {
		DescriptionProduct dProduct = descriptionRepository.findByProduct(product.getId());
		descriptionRepository.delete(dProduct);
		descriptionRepository.saveAndFlush(description);
	}
}
