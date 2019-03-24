package com.shan.app.repository.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shan.app.domain.QCategory;
import com.shan.app.repository.admin.AdminCategoryRepositoryCustom;

public class AdminCategoryRepositoryImpl implements AdminCategoryRepositoryCustom {

	@Autowired
	private JPAQueryFactory queryFactory;
	
	@Override
	public Integer getMaxOrdByLevel(Integer level) {
		QCategory qCategory = QCategory.category1;
		
		return queryFactory.select(qCategory.ord.max().coalesce(0))
							.from(qCategory)
							.where(qCategory.level.eq(level))
							.fetchFirst();
	}

}
