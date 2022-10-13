package com.lucistore.lucistorebe.entity.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.lucistore.lucistorebe.utility.ETransactionStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {
	@Id
	@Column(name="id", updatable=false, unique=true)
	private Long id;
	
	@OneToOne(optional = false)
	@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
	private Order order;
	
	private String data;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ETransactionStatus status;

	public Transaction(Order order, String data) {
		this.id = order.getId();
		this.data = data;
		this.status = ETransactionStatus.PAID;
	}
}
