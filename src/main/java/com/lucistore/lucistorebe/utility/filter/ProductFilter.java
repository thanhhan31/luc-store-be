package com.lucistore.lucistorebe.utility.filter;

import java.util.List;

import com.lucistore.lucistorebe.utility.EProductStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductFilter {
	List<Long> idsCategory;
	String searchName;
	String searchDescription;
	EProductStatus status;
	Long minPrice;
	Long maxPrice;
	
	public ProductFilter(List<Long> idsCategory, String searchName, String searchDescription, EProductStatus status, 
			Long minPrice, Long maxPrice) {
		this.idsCategory = idsCategory;
		this.searchName = searchName;
		this.searchDescription = searchDescription;
		this.status = status;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}
}
