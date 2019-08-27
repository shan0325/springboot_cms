package com.shan.app.repository.admin;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.querydsl.core.Tuple;

public interface AdminUserRepositoryCustom {
	
	List<Tuple> findList(Pageable pageable);

}
