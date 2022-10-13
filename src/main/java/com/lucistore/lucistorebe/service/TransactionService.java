package com.lucistore.lucistorebe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.controller.advice.exception.CommonRuntimeException;
import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.entity.order.Transaction;
import com.lucistore.lucistorebe.repo.OrderRepo;
import com.lucistore.lucistorebe.repo.TransactionRepo;

@Service
public class TransactionService {
	@Autowired
	TransactionRepo transactionRepo;
	
	@Autowired
	OrderRepo orderRepo;
	
	public void assignTransaction(Long idOrder, String transId) {
		Order o = orderRepo.getReferenceById(idOrder);
		
		Transaction t = new Transaction(o, transId);
		transactionRepo.save(t);
	}
	
	public Transaction getByIdOrder(Long idOrder) {
		return transactionRepo.findById(idOrder).orElseThrow(() -> new CommonRuntimeException("No transaction found"));
	}
	
	public void setRefund(Long idOrder) {
		transactionRepo.setRefund(idOrder);
	}
}
