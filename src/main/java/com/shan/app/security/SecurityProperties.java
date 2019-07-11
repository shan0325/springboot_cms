package com.shan.app.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("security")
public class SecurityProperties {
	
	private JwtProperties jwt;
	
	public JwtProperties getJwt() {
		return jwt;
	}
	
	public void setJwt(JwtProperties jwt) {
		this.jwt = jwt;
	}
	
	@Getter
	@Setter
	public static class JwtProperties {
		
		// Authorization Server Properties
		private Resource keyStore;
		private String keyStorePassword;
		private String keyPairAlias;
		private String keyPairPassword;

		// Resource Server Properties
		private Resource publicKey;
	}

}
