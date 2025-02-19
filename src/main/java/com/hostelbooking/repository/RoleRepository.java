package com.hostelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.hostelbooking.entity.Role;
import com.hostelbooking.enums.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(RoleType name);
}
