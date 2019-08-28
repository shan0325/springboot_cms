package com.shan.app.repository.admin;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.shan.app.domain.User;

public interface AdminUserRepositoryCustom {
	
	PageImpl<User> findList(Pageable pageable);

}
