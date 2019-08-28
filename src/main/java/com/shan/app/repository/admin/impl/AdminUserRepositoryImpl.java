package com.shan.app.repository.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shan.app.domain.QUser;
import com.shan.app.domain.User;
import com.shan.app.repository.admin.AdminUserRepositoryCustom;

public class AdminUserRepositoryImpl implements AdminUserRepositoryCustom {
	
	@Autowired
	private JPAQueryFactory queryFactory;

	@Override
	public PageImpl<User> findList(Pageable pageable) {
		QUser qUser = QUser.user;
		
		QueryResults<User> fetchResults = queryFactory
											.select(qUser)
											.from(qUser)
												.limit(pageable.getPageSize())
												.offset(pageable.getOffset())
											.fetchResults();
		
		return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
	}

}
