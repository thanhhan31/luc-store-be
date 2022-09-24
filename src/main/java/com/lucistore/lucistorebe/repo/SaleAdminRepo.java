package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.user.SaleAdmin;

@Repository
public interface SaleAdminRepo extends JpaRepository<SaleAdmin, Long> {

}
