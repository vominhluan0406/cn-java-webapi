package com.shop.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

}
