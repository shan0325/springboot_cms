package com.shan.app.config;

import java.security.KeyPair;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.shan.app.security.admin.AdminCustomUserDetailsService;

public class AuthorizationServerConfig {
	
	@Configuration
	@EnableAuthorizationServer
	public static class AdminAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
		
		@Value("${resouce.id:spring-boot-application}")
		private String resourceId;
		
		@Value("${access_token.validity_period:3600}")
		int accessTokenValiditySeconds = 3600;
		
		@Autowired
		private AuthenticationManager authenticationManager;
		
		@Autowired
		private PasswordEncoder passwordEncoder;
		
		@Resource(name="adminCustomUserDetailsService")
		private AdminCustomUserDetailsService adminCustomUserDetailsService;
		
		@Autowired
		private DataSource dataSource;
		
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//			clients.inMemory()
//					.withClient("spring") // 예 : facebook clientId 와 같음
//					.secret(passwordEncoder.encode("1234")) // 예 : facebook secret 와 같음
//					.authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
//					.scopes("read", "write", "trust")
//					.accessTokenValiditySeconds(1*60*60)
//					.refreshTokenValiditySeconds(6*60*60);
			
			clients.jdbc(dataSource);
		}
		
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.tokenStore(tokenStore())
						.authenticationManager(authenticationManager)
						.accessTokenConverter(accessTokenConverter());
		}
		
		@Bean
		public TokenStore tokenStore() {
//			return new InMemoryTokenStore();
//			return new JdbcTokenStore(dataSource);
			return new JwtTokenStore(accessTokenConverter());
		}

		@Bean
		public JwtAccessTokenConverter accessTokenConverter() {
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("server.jks"), "123456".toCharArray()).getKeyPair("auth", "123456".toCharArray());
			converter.setKeyPair(keyPair);
			return converter;
		}
		
		@Bean
		@Primary
		public DefaultTokenServices tokenService() {
			DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
			defaultTokenServices.setTokenStore(tokenStore());
			defaultTokenServices.setSupportRefreshToken(true);
			return defaultTokenServices;
		}
		
		
	}
	
}
