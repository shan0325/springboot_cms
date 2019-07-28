package com.shan.app.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	private Set<String> ignorePath = new HashSet<>();

	public JwtAuthenticationFilter() {
		super("/**");
		ignorePath.add("POST/spring-admin/auth/refreshToken");
	}
	
	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String method = request.getMethod();
		String uri = request.getRequestURI();
		logger.info(method + uri);

		return !ignorePath.contains(method + uri);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		String header = request.getHeader("Authorization");
		if(header == null || !header.startsWith("Bearer")) {
			throw new UnauthorizedUserException("No JWT token found in request headers");
		}
		
		String authToken = header.substring(7);
		logger.info("authToken : " + authToken);
		JwtAuthenticationToken authRequest = new JwtAuthenticationToken(authToken);
		
		return getAuthenticationManager().authenticate(authRequest);
	}

}
