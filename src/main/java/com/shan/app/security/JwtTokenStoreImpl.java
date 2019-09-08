package com.shan.app.security;

import java.time.LocalDateTime;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.shan.app.domain.Manager;
import com.shan.app.domain.User;
import com.shan.app.service.admin.AdminManagerService;
import com.shan.app.service.admin.AdminUserService;

public class JwtTokenStoreImpl extends JwtTokenStore {
	
	@Autowired
	private HttpServletRequest request;
	
	@Resource(name="adminUserService")
	private AdminUserService adminUserService;
	
	@Resource(name="adminManagerService")
	private AdminManagerService adminManagerService;

	public JwtTokenStoreImpl(JwtAccessTokenConverter jwtTokenEnhancer) {
		super(jwtTokenEnhancer);
	}

	@Transactional
	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		// 인증 성공 시 RefreshToken DB 저장
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		String isManager = request.getParameter("isManager");
		if("Y".contentEquals(isManager)) {
			Manager manager = adminManagerService.getManager(userDetails.getUsername());
			manager.setRefreshToken(refreshToken.getValue());
			manager.setRefreshTokenRegDate(LocalDateTime.now());
		} else {
			User user = adminUserService.getUser(userDetails.getUsername());
			user.setRefreshToken(refreshToken.getValue());
			user.setRefreshTokenRegDate(LocalDateTime.now());
		}
	}
	
}
