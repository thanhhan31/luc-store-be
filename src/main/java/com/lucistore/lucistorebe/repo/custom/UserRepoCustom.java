package com.lucistore.lucistorebe.repo.custom;

import org.springframework.data.domain.Page;

import com.lucistore.lucistorebe.entity.user.User;
import com.lucistore.lucistorebe.utility.filter.PagingInfo;
import com.lucistore.lucistorebe.utility.filter.UserFilter;

public interface UserRepoCustom {

	Page<User> search(UserFilter filter, PagingInfo pagingInfo);

}