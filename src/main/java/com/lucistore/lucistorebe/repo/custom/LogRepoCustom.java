package com.lucistore.lucistorebe.repo.custom;

import java.util.Date;
import java.util.List;

import com.lucistore.lucistorebe.entity.Log;
import com.lucistore.lucistorebe.utility.ELogType;
import com.lucistore.lucistorebe.utility.PageWithJpaSort;

public interface LogRepoCustom {

	List<Log> search(Long idUser, Date beginDate, Date endDate, ELogType logType, String searchContent,
			PageWithJpaSort page);

	Long searchCount(Long idUser, Date beginDate, Date endDate, ELogType logType, String searchContent);

}