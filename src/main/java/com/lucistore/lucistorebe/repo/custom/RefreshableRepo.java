package com.lucistore.lucistorebe.repo.custom;

public interface RefreshableRepo<T> {
	
	void refresh(T t);
	
}
