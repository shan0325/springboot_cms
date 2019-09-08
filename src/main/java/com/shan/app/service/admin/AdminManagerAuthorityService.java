package com.shan.app.service.admin;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.shan.app.domain.Manager;
import com.shan.app.domain.ManagerAuthority;
import com.shan.app.repository.admin.AdminManagerAuthorityRepository;

@Service
@Transactional
public class AdminManagerAuthorityService {

	@Resource(name="adminManagerAuthorityRepository")
	private AdminManagerAuthorityRepository adminManagerAuthorityRepository;

	public List<ManagerAuthority> getManagerAuthoritys(Manager manager) {
		
		return adminManagerAuthorityRepository.findByManager(manager);
	}
}
