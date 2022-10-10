package com.lucistore.lucistorebe.utility;

public enum EOrderStatus {
	WAIT_FOR_PAYMENT, // can cancel
	WAIT_FOR_CONFIRM, // can cancel
	WAIT_FOR_SEND, // can cancel
	DELIVERING,
	DELIVERED, // review order to complete order
	COMPLETED,
	CANCELLED,
}
