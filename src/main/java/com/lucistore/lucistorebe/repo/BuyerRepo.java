package com.lucistore.lucistorebe.repo;

import javax.annotation.Nullable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.repo.custom.BuyerRepoCustom;

@Repository
public interface BuyerRepo extends JpaRepository<Buyer, Long>, BuyerRepoCustom {
	Boolean existsByUser_Username(String username);

	Boolean existsByUser_Email(String email);
	
	Boolean existsByUser_Phone(String phone);
	
	@Nullable
	Buyer findByUser_Email(String email);
	
	@Nullable
	Buyer findByUser_Phone(String phone);
}
