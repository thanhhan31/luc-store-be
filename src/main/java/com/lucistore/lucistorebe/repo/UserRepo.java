package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.user.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	@Nullable
	User findByUsername(String username);
	
	@Nullable
	User findByPhone(String phone);
	
	@Nullable
	User findByEmail(String email);
}
