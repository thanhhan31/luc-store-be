package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.user.UserRole;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, String> {
	
}
