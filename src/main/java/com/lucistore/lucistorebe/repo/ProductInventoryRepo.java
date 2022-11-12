package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lucistore.lucistorebe.entity.product.ProductInventory;
import com.lucistore.lucistorebe.repo.custom.ProductInventoryRepoCustom;

public interface ProductInventoryRepo extends JpaRepository<ProductInventory, Long>, ProductInventoryRepoCustom {

}