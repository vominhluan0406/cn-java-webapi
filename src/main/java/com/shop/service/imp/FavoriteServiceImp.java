package com.shop.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dto.FavoriteDTO;
import com.shop.entity.Customer;
import com.shop.entity.Favorite;
import com.shop.entity.Product;
import com.shop.respository.CustomerRepository;
import com.shop.respository.FavoriteRepository;
import com.shop.respository.ProductRepository;
import com.shop.service.FavoriteService;

@Service
public class FavoriteServiceImp implements FavoriteService {

	@Autowired
	private FavoriteRepository favoriteRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public void addFavorite(FavoriteDTO favoriteDTO) {

		Product product = productRepository.findById(favoriteDTO.getProduct_id()).get();
		Customer customer = customerRepository.findById(favoriteDTO.getUser_id()).get();

		Favorite favorite = new Favorite();
		favorite.setCustomer(customer);
		favorite.setProduct(product);

		favoriteRepository.save(favorite);
	}

	@Override
	public void unFavorite(FavoriteDTO favoriteDTO) {
		Favorite favorite = favoriteRepository.findByUserAndProduct(favoriteDTO.getUser_id(),
				favoriteDTO.getProduct_id());
		favoriteRepository.delete(favorite);
	}

	@Override
	public boolean checkExist(FavoriteDTO favoriteDTO) {
		if (favoriteRepository.countByUserAndProduct(favoriteDTO.getUser_id(), favoriteDTO.getProduct_id()) == 0) {
			return false;
		}
		return true;
	}

	@Override
	public List<Long> getByUser(long user_id) {
		List<Favorite> favorites = favoriteRepository.findByUser(user_id);
		List<Long> favoriteMap = new ArrayList<Long>();
		for (Favorite favorite : favorites) {
			favoriteMap.add(favorite.getProduct().getId());
		}
		return favoriteMap;
	}
}
