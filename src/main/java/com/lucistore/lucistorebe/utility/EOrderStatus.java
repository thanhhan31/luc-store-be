package com.lucistore.lucistorebe.utility;

public enum EOrderStatus {
	WAIT_FOR_PAYMENT, // can cancel
	WAIT_FOR_CONFIRM, // can cancel - can refund
	WAIT_FOR_SEND, // can cancel - can refund
	DELIVERING,
	DELIVERED, // review order to complete order
	COMPLETED,
	CANCELLED,
}
