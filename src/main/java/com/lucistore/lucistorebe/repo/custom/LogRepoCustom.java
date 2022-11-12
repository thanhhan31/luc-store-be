package com.lucistore.lucistorebe.repo.custom;

import org.springframework.data.domain.Page;

import com.lucistore.lucistorebe.entity.Log;
import com.lucistore.lucistorebe.utility.filter.LogFilter;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;

public interface LogRepoCustom {

	Page<Log> search(LogFilter filter, PagingInfo pagingInfo);

}