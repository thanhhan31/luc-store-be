package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lucistore.lucistorebe.entity.order.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
	@Transactional
	@Modifying
	@Query(value = "UPDATE Transaction t set t.status = com.lucistore.lucistorebe.utility.ETransactionStatus.REFUNDED WHERE id = ?1")
	int setRefund(Long idOrder);
}
