package com.shan.app.repository.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shan.app.domain.Authority;
import com.shan.app.domain.QUser;
import com.shan.app.domain.QUserAuthority;
import com.shan.app.domain.UserAuthority;
import com.shan.app.repository.admin.AdminUserRepositoryCustom;
import com.shan.app.service.admin.dto.AuthorityDTO;
import com.shan.app.service.admin.dto.UserDTO;
import com.shan.app.service.admin.dto.UserDTO.Response;

public class AdminUserRepositoryImpl implements AdminUserRepositoryCustom {
	
	@Autowired
	private JPAQueryFactory queryFactory;

	@Override
	public List<Tuple> findList(Pageable pageable) {
		QUser qUser = QUser.user;
		QUserAuthority qUserAuthority = QUserAuthority.userAuthority;
		
		List<Response> fetch = queryFactory
									.select(Projections.fields(UserDTO.Response.class, qUser.userId, qUser.hp))
									.from(qUser)
										.limit(pageable.getPageSize())
										.offset(pageable.getOffset())
									.fetch();
		System.out.println("fetch : " + fetch);
		
		List<Tuple> result = queryFactory
								.select(qUser, qUserAuthority.authority)
								.from(qUser)
									.leftJoin(qUserAuthority)
										.on(qUser.id.eq(qUserAuthority.user.id))
									.limit(pageable.getPageSize())
									.offset(pageable.getOffset())
								.fetch();

		return result;
//		return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
	}

}
