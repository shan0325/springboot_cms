package com.shan.app.security;

import java.time.LocalDateTime;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.shan.app.domain.User;
import com.shan.app.service.admin.AdminUserService;

public class JwtTokenStoreImpl extends JwtTokenStore {
	
	@Resource(name="adminUserService")
	private AdminUserService adminUserService;

	public JwtTokenStoreImpl(JwtAccessTokenConverter jwtTokenEnhancer) {
		super(jwtTokenEnhancer);
	}

	@Transactional
	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		// 인증 성공 시 RefreshToken DB 저장
		SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
		
		User user = adminUserService.getUser(securityUser.getUsername());
		user.setRefreshToken(refreshToken.getValue());
		user.setRefreshTokenRegDate(LocalDateTime.now());
	}
	
}
