package com.shop.service;

import com.shop.entity.DescriptionProduct;
import com.shop.entity.Product;

public interface DescriptionService {

	void addNew(DescriptionProduct description);

	void updateDescription(DescriptionProduct description, Product product);

}
