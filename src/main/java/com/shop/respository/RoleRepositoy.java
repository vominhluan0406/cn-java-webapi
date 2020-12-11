package com.shop.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.entity.Role;

@Repository
public interface RoleRepositoy extends JpaRepository<Role, Long> {

}
