package com.lucistore.lucistorebe.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.lucistore.lucistorebe.entity.product.ProductInventory;

public interface ProductInventoryRepo extends JpaRepository<ProductInventory, Long>, PagingAndSortingRepository<ProductInventory, Long> {
    // @Query("SELECT p FROM ProductInventory p WHERE p.id_importer = :idImporter ORDER BY p.importTime DESC")
    // List<ProductInventory> findAllByIdImporter(@Param("idImporter") Long id);

    // @Query("SELECT p FROM ProductInventory p WHERE p.idProduct = :idProduct ORDER BY p.importTime DESC")
    // List<ProductInventory> findAllByIdProduct(@Param("idProduct") Long id);
}