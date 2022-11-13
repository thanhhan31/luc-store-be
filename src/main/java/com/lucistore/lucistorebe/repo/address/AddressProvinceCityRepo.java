package com.lucistore.lucistorebe.repo.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.address.AddressProvinceCity;

@Repository
public interface AddressProvinceCityRepo extends JpaRepository<AddressProvinceCity, Long> {
	
}
