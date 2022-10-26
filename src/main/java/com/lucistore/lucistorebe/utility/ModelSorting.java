package com.lucistore.lucistorebe.utility;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

import com.lucistore.lucistorebe.entity.Log_;
import com.lucistore.lucistorebe.entity.product.Product_;

public final class ModelSorting {
	private ModelSorting() { }
	
	/**
	 * Sort by combination of selected attribute
	 * <br>
	 * <i>Example: sortBy = (1+2+4) = 7 => sort by price, view and averageRating attribute
	 * @param sortBy null for unsorted and possible value is 1, 2, 4, 8
	 * @param sortDescending true if sort in descending order, otherwise ascending
	 * @return Sort object
	 * <br>
	 * sortBy possible value
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; 1: by price (minPrice)
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; 2: by nvisit
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; 4: by nsold
	 * * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; 8: by createdDate
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; 16: by lastModifiedDate
	 */
	public static Sort getProductSort(Integer sortBy, Boolean sortDescending)  {		 
		Sort sort = Sort.unsorted();
		
		if (sortBy != null) {
			if (sortDescending == null || sortDescending.booleanValue() == false) { //ASC
				if (sortBy >= 16) {
					sort = sort.and(JpaSort.of(Product_.lastModifiedDate).ascending());
					sortBy -= 16;
				}
				if (sortBy >= 4) {
					sort = sort.and(JpaSort.of(Product_.nsold).ascending());
					sortBy -= 4;
				}
				if (sortBy >= 2) {
					sort = sort.and(JpaSort.of(Product_.nvisit).ascending());
					sortBy -= 2;
				}
				if (sortBy >= 1) {
					sort = sort.and(JpaSort.of(Product_.minPrice).ascending());
					sortBy -= 1;
				}
			}
			else {	//DESC
				if (sortBy >= 16) {
					sort = sort.and(JpaSort.of(Product_.lastModifiedDate).descending());
					sortBy -= 16;
				}
				if (sortBy >= 8) {
					sort = sort.and(JpaSort.of(Product_.createdDate).descending());
					sortBy -= 8;
				}
				if (sortBy >= 4) {
					sort = sort.and(JpaSort.of(Product_.nsold).descending());
					sortBy -= 4;
				}
				if (sortBy >= 2) {
					sort = sort.and(JpaSort.of(Product_.nvisit).descending());
					sortBy -= 2;
				}
				if (sortBy >= 1) {
					sort = sort.and(JpaSort.of(Product_.minPrice).descending());
					sortBy -= 1;
				}
			}
		}
		return sort;
	}
	
	/**
	 * Sort by combination of selected attribute
	 * <br>
	 * <i>Example: sortBy = (1+2+4) = 7 => sort by price, view and averageRating attribute
	 * @param sortBy null for unsorted and possible value is 1, 2, 4, 8
	 * @param sortDescending true if sort in descending order, otherwise ascending
	 * @return Sort object
	 * <br>
	 * sortBy possible value
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; 1: by date
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp; 2: by logType
	 */
	public static Sort getLogSort(Boolean sortByDateDescending)  {
		if (sortByDateDescending != null) {
			if (sortByDateDescending.booleanValue())
				return JpaSort.of(Log_.date).descending();
			else
				return JpaSort.of(Log_.date).ascending();
		}
		else
			return Sort.unsorted();
	}
}
