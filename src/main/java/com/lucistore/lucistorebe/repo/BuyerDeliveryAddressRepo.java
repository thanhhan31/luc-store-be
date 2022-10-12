package com.lucistore.lucistorebe.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lucistore.lucistorebe.entity.user.buyer.Buyer;
import com.lucistore.lucistorebe.entity.user.buyer.BuyerDeliveryAddress;

public interface BuyerDeliveryAddressRepo extends JpaRepository<BuyerDeliveryAddress, Long> {
    List<BuyerDeliveryAddress> findAllByBuyer(Buyer buyer, Sort sort);
}
    
