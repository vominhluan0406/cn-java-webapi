package com.shop.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.entity.Categories;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long>{

}
