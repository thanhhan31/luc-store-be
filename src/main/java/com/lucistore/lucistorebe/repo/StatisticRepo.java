package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.order.Order;
import com.lucistore.lucistorebe.repo.custom.StatisticRepoCustom;


public interface StatisticRepo extends StatisticRepoCustom {
	
}
