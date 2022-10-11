package com.lucistore.lucistorebe.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lucistore.lucistorebe.entity.pk.BuyerCartDetailPK;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerCartDetail;

public interface BuyerCartDetailRepo extends JpaRepository<BuyerCartDetail, BuyerCartDetailPK> {
    @Query("SELECT bcd FROM BuyerCartDetail bcd WHERE bcd.id.idBuyer = :idBuyer")
    List<BuyerCartDetail> findAllByIdBuyer(Long idBuyer);
}
    
