package com.lucistore.lucistorebe.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucistore.lucistorebe.entity.pk.BuyerCartDetailPK;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerCartDetail;

public interface BuyerCartDetailRepo extends JpaRepository<BuyerCartDetail, BuyerCartDetailPK> {
    List<BuyerCartDetail> findAllByIdBuyer(Long idBuyer);
}
    
