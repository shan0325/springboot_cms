package com.shan.app.service.admin;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.shan.app.domain.User;
import com.shan.app.domain.UserAuthority;
import com.shan.app.repository.admin.AdminUserAuthorityRepository;

@Service
@Transactional
public class AdminUserAuthorityService {
	
	@Resource(name="adminUserAuthorityRepository")
	private AdminUserAuthorityRepository adminUserAuthorityRepository;

	public List<UserAuthority> getUserAuthoritys(User user) {
		
		return adminUserAuthorityRepository.findByUser(user);
	}

	
}
