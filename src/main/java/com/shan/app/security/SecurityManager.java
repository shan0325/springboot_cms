package com.shan.app.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.shan.app.domain.Manager;
import com.shan.app.domain.ManagerAuthority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityManager extends User {

	private static final long serialVersionUID = 1L;
	public static final String ROLE_PREFIX = "ROLE_";
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityManager.class);
	
	public SecurityManager(Manager manager, List<ManagerAuthority> managerAuthoritys) {
		super(manager.getManagerId(), manager.getPassword(), makeGrantedAuthority(managerAuthoritys));
	}

	private static List<GrantedAuthority> makeGrantedAuthority(List<ManagerAuthority> managerAuthoritys) {
		List<GrantedAuthority> list = new ArrayList<>();
		if(managerAuthoritys != null) {
			managerAuthoritys.forEach(managerAuthority -> {
				list.add(new SimpleGrantedAuthority(ROLE_PREFIX + managerAuthority.getAuthority().getAuthority()));
			});
		}
		logger.debug("SecurityManager managerAuthoritys : " + list);
		return list;
	}
	
	public static SecurityManager getSecurityManager() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (SecurityManager) authentication.getPrincipal();
	}
}
