package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.user.buyer.Buyer;

@Repository
public interface BuyerRepo extends JpaRepository<Buyer, Long> {
	
}
