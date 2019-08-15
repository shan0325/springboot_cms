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

import com.shan.app.domain.UserAuthority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityUser extends User {

	private static final long serialVersionUID = 1L;
	public static final String ROLE_PREFIX = "ROLE_";
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityUser.class);
	
	public SecurityUser(com.shan.app.domain.User user, List<UserAuthority> userAuthoritys) {
		super(user.getUserId(), user.getPassword(), makeGrantedAuthority(userAuthoritys));
	}

	private static List<GrantedAuthority> makeGrantedAuthority(List<UserAuthority> userAuthoritys) {
		List<GrantedAuthority> list = new ArrayList<>();
		if(userAuthoritys != null) {
			userAuthoritys.forEach(userAuthority -> {
				list.add(new SimpleGrantedAuthority(ROLE_PREFIX + userAuthority.getAuthority().getAuthority()));
			});
		}
		logger.debug("SecurityUser userAuthoritys : " + list);
		return list;
	}
	
	public static SecurityUser getSecurityUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (SecurityUser) authentication.getPrincipal();
	}
}
