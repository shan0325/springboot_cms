package com.shan.app.security.admin;

import java.util.Optional;

import javax.annotation.Resource;
import javax.jdo.annotations.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shan.app.domain.User;
import com.shan.app.error.EntityNotFoundException;
import com.shan.app.repository.admin.AdminUserRepository;
import com.shan.app.security.SecurityUser;
import com.shan.app.security.UserNotActivatedException;

@Service
public class AdminCustomUserDetailsService implements UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(AdminCustomUserDetailsService.class);
	
	@Resource(name="adminUserRepository")
	private AdminUserRepository adminUserRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		logger.debug("Authenticating {}", userId);
		
		Optional<User> userFromDatabase = adminUserRepository.findOneWithUserAuthoritiesByUserId(userId);
		return userFromDatabase.map(user -> {
			if("Y".equals(user.getState())) {
				throw new UserNotActivatedException("User " + userId + "was not activated");
			}
			return new SecurityUser(user);
		}).orElseThrow(() -> new EntityNotFoundException(User.class, "userId", userId));
	}

}
