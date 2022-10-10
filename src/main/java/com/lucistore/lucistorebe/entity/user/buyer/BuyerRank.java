package com.lucistore.lucistorebe.entity.user.buyer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "buyer_rank")
public class BuyerRank {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "discount_rate")
	private Double discountRate;

	@Column(name = "threshold")
	private Integer threshold;

	@OneToOne
	@JoinColumn(name = "id_next_rank")
	private BuyerRank nextRank;
}
