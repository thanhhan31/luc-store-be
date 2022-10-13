package com.lucistore.lucistorebe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucistore.lucistorebe.repo.OrderRepo;

@Service
public class PaymentService {
	@Autowired
	OrderRepo orderRepo;
	
	public void paymentConfirm( Long idOrder, Long idBuyer) {
		
	}
	
	public boolean confirm(Long idOrder) {
		return orderRepo.confirmPayment(idOrder) == 1;
	}
}
