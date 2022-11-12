package com.lucistore.lucistorebe.utility.filter;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductInventoryFilter {
	Long idProduct;
	Long idProductVariation;
	Long idImporter;
	Date importTimeFrom;
	Date importTimeTo;
	
	public ProductInventoryFilter(Long idProduct, Long idProductVariation, Long idImporter, Date importTimeFrom,
			Date importTimeTo) {
		this.idProduct = idProduct;
		this.idProductVariation = idProductVariation;
		this.idImporter = idImporter;
		this.importTimeFrom = importTimeFrom;
		this.importTimeTo = importTimeTo;
	}
}
