package com.lucistore.lucistorebe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lucistore.lucistorebe.entity.MediaResource;

@Repository
public interface MediaResourceRepo extends JpaRepository<MediaResource, Long> {
	
}
