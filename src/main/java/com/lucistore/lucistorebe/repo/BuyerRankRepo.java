package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.user.buyer.BuyerRank;

@Repository
public interface BuyerRankRepo extends JpaRepository<BuyerRank, Long> {
	
}
