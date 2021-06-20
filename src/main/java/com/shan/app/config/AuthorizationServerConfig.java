package com.shan.app.config;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.security.KeyPair;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.shan.app.security.CustomUserDetailsService;
import com.shan.app.security.JwtTokenStoreImpl;
import com.shan.app.security.SecurityProperties;


/**
 * 참초 사이트
 * https://blog.marcosbarbero.com/centralized-authorization-jwt-spring-boot2/
 * 
 * 
 *
 */
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(SecurityProperties.class)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
		
	private DataSource dataSource;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private SecurityProperties securityProperties;
	private CustomUserDetailsService customUserDetailsService;
	
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	private TokenStore tokenStore;
	
	public AuthorizationServerConfig(final DataSource dataSource, final PasswordEncoder passwordEncoder,
											final AuthenticationManager authenticationManager, final SecurityProperties securityProperties,
											final CustomUserDetailsService customUserDetailsService) {
		this.dataSource = dataSource;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.securityProperties = securityProperties;
		this.customUserDetailsService = customUserDetailsService;
	}
	
	@Bean
	public TokenStore tokenStore() {
		if(this.tokenStore == null) {
//				this.tokenStore = new InMemoryTokenStore();
//				this.tokenStore = new JdbcTokenStore(dataSource);
			this.tokenStore = new JwtTokenStoreImpl(accessTokenConverter());
		}
		return this.tokenStore;
	}
	
	@Bean
	public DefaultTokenServices tokenService(final TokenStore tokenStore, final ClientDetailsService clientDetailsService) {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setTokenStore(tokenStore);
		tokenServices.setClientDetailsService(clientDetailsService);
		tokenServices.setAuthenticationManager(this.authenticationManager);
		return tokenServices;
	}

	/**
	 * jwt토큰 방식의 좋은점은 db가 필요 없다는것이다. 서명을 통한 인증만 하면 된다.
	 * 기본적으로 JwtAccessTokenConverter 만 등록해도 상관은 없다.
	 * 하지만 여기선 KeyPair를 등록하는 방식으로 한다.
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		if(this.jwtAccessTokenConverter != null) {
			return this.jwtAccessTokenConverter;
		}
		
		SecurityProperties.JwtProperties jwtProperties = this.securityProperties.getJwt();
		KeyPair keyPair = keyPair(jwtProperties, keyStoreKeyFactory(jwtProperties));
		
		this.jwtAccessTokenConverter = new JwtAccessTokenConverter();
		this.jwtAccessTokenConverter.setKeyPair(keyPair);
		this.jwtAccessTokenConverter.setVerifierKey(getPublicKeyAsString(jwtProperties));
		return this.jwtAccessTokenConverter;
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//			clients.inMemory()
//					.withClient("spring") // 예 : facebook clientId 와 같음
//					.secret(passwordEncoder.encode("1234")) // 예 : facebook secret 와 같음
//					.authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
//					.scopes("read", "write", "trust")
//					.accessTokenValiditySeconds(1*60*60)
//					.refreshTokenValiditySeconds(6*60*60);
		
		clients.jdbc(this.dataSource);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(this.authenticationManager)
					.accessTokenConverter(accessTokenConverter())
					.userDetailsService(this.customUserDetailsService)
					.tokenStore(tokenStore());
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.passwordEncoder(this.passwordEncoder)
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()");
	}
	
	private KeyPair keyPair(SecurityProperties.JwtProperties jwtProperties, KeyStoreKeyFactory keyStoreKeyFactory) {
		return keyStoreKeyFactory.getKeyPair(jwtProperties.getKeyPairAlias(), jwtProperties.getKeyPairPassword().toCharArray());
	}

	private KeyStoreKeyFactory keyStoreKeyFactory(SecurityProperties.JwtProperties jwtProperties) {
		return new KeyStoreKeyFactory(jwtProperties.getKeyStore(), jwtProperties.getKeyStorePassword().toCharArray());
	}
	
	private String getPublicKeyAsString(SecurityProperties.JwtProperties jwtProperties) {
		try {
			return IOUtils.toString(jwtProperties.getPublicKey().getInputStream(), UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
		
}
