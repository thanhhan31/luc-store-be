package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.Log;
import com.lucistore.lucistorebe.repo.custom.LogRepoCustom;

@Repository
public interface LogRepo extends JpaRepository<Log, Long>, LogRepoCustom {
	
}
