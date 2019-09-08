package com.shan.app.security.admin;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.jdo.annotations.Transactional;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shan.app.domain.Manager;
import com.shan.app.domain.ManagerAuthority;
import com.shan.app.domain.User;
import com.shan.app.domain.UserAuthority;
import com.shan.app.error.EntityNotFoundException;
import com.shan.app.repository.admin.AdminManagerRepository;
import com.shan.app.repository.admin.AdminUserRepository;
import com.shan.app.security.SecurityUser;
import com.shan.app.security.SecurityManager;
import com.shan.app.security.UserNotActivatedException;
import com.shan.app.service.admin.AdminManagerAuthorityService;
import com.shan.app.service.admin.AdminUserAuthorityService;

@Service
public class AdminCustomUserDetailsService implements UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(AdminCustomUserDetailsService.class);
	
	@Autowired
	private HttpServletRequest request;
	
	@Resource(name="adminUserRepository")
	private AdminUserRepository adminUserRepository;
	
	@Resource(name="adminUserAuthorityService")
	private AdminUserAuthorityService adminUserAuthorityService;
	
	@Resource(name="adminManagerRepository")
	private AdminManagerRepository adminManagerRepository;
	
	@Resource(name="adminManagerAuthorityService")
	private AdminManagerAuthorityService adminManagerAuthorityService;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		logger.debug("Authenticating {}", userId);

		String isManager = request.getParameter("isManager");
		if("Y".contentEquals(isManager)) {
			Optional<Manager> managerFromDatabase = adminManagerRepository.findByManagerId(userId);
			return managerFromDatabase.map(manager -> {
				if(!"Y".contentEquals(manager.getState())) {
					throw new UserNotActivatedException("Manager " + manager + " was not activated");
				}
				
				List<ManagerAuthority> managerAuthoritys = adminManagerAuthorityService.getManagerAuthoritys(manager);
				return new SecurityManager(manager, managerAuthoritys);
			}).orElseThrow(() -> new EntityNotFoundException(Manager.class, "managerId", userId));
		} else {
			Optional<User> userFromDatabase = adminUserRepository.findByUserId(userId);
			return userFromDatabase.map(user -> {
				if(!"Y".equals(user.getState())) {
					throw new UserNotActivatedException("User " + userId + " was not activated");
				}
				
				List<UserAuthority> userAuthoritys = adminUserAuthorityService.getUserAuthoritys(user);
				return new SecurityUser(user, userAuthoritys);
			}).orElseThrow(() -> new EntityNotFoundException(User.class, "userId", userId));
		}
	}

}
