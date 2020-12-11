package com.shop.service;

import java.util.List;

import com.shop.dto.FavoriteDTO;

public interface FavoriteService {

	void addFavorite(FavoriteDTO favoriteDTO);

	void unFavorite(FavoriteDTO favoriteDTO);

	boolean checkExist(FavoriteDTO favoriteDTO);

	List<Long> getByUser(long parseLong);

}
