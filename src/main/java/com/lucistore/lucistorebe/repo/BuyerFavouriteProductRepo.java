package com.lucistore.lucistorebe.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lucistore.lucistorebe.entity.pk.BuyerFavouriteProductPK;
import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerFavouriteProduct;

public interface BuyerFavouriteProductRepo extends JpaRepository<BuyerFavouriteProduct, BuyerFavouriteProductPK> {
    List<BuyerFavouriteProduct> findAllByBuyer(Buyer buyer, Sort sort);
}
    
