package com.shan.app.security;

import java.util.ArrayList;
import java.util.List;

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
	
	public SecurityUser(com.shan.app.domain.User user) {
		super(user.getUserId(), user.getPassword(), makeGrantedAuthority(user.getUserAuthoritys()));
	}

	private static List<GrantedAuthority> makeGrantedAuthority(List<UserAuthority> userAuthoritys) {
		List<GrantedAuthority> list = new ArrayList<>();
		userAuthoritys.forEach(userAuthority -> {
			list.add(new SimpleGrantedAuthority(ROLE_PREFIX + userAuthority.getAuthority().getAuthority()));
		});
		return list;
	}
	
	public static SecurityUser getSecurityUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (SecurityUser) authentication.getPrincipal();
	}
}
