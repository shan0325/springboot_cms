package com.shan.app.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.shan.app.security.AjaxSessionCheckFilter;
import com.shan.app.security.CustomAuthenticationFailureHandler;
import com.shan.app.security.CustomAuthenticationSuccessHandler;
import com.shan.app.security.admin.AdminCustomUserDetailsService;



@Configuration
public class SecurityConfig {

	@EnableWebSecurity
	public static class AdminConfigurationAdapter extends WebSecurityConfigurerAdapter {
		
		@Resource(name="adminCustomUserDetailsService")
		private AdminCustomUserDetailsService adminCustomUserDetailsService;
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(adminCustomUserDetailsService).passwordEncoder(passwordEncoder());
		}
		
		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/css/**", "/**/*.css",  "/script/**", "/**/*.js",  "image/**", "/fonts/**", "lib/**");
		}
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.csrf().disable()
				.headers().frameOptions().disable()
					.and()
				.addFilterAfter(ajaxSessionCheckFilter(), ExceptionTranslationFilter.class)
				.authorizeRequests()
					.antMatchers("/h2-console/**").permitAll()
					.antMatchers("/spring-admin/login").permitAll()
					.antMatchers("/spring-admin/auth/**").permitAll()
					.antMatchers("/spring-admin/api/**").authenticated();
//					.and()
//				.formLogin()
//					.loginPage("/spring-admin/login") //로그인 페이지
//					.usernameParameter("userId")
//					.passwordParameter("password")
//					.loginProcessingUrl("/spring-admin/api/login") //로그인 폼 action
//					.permitAll()
//					.and()
//				.logout()
//					.logoutUrl("/spring-admin/logout")
//					.permitAll();
		}
		
		@Bean
		public AjaxSessionCheckFilter ajaxSessionCheckFilter() {
			AjaxSessionCheckFilter ajaxSessionCheckFilter = new AjaxSessionCheckFilter();
			ajaxSessionCheckFilter.setAjaxHeader("AJAX");
			return ajaxSessionCheckFilter;
		}
		
		@Bean
		public AuthenticationSuccessHandler successHandler() {
			CustomAuthenticationSuccessHandler successHandler = new CustomAuthenticationSuccessHandler();
			successHandler.setTargetUrlParameter("loginRedirect");
			successHandler.setUseReferer(true);
			successHandler.setDefaultUrl("/spring-admin/main");
			return successHandler;
		}
		
		@Bean
		public AuthenticationFailureHandler failureHandler() {
			CustomAuthenticationFailureHandler failureHandler = new CustomAuthenticationFailureHandler();
			failureHandler.setLoginidname("userId");
			failureHandler.setLoginpasswdname("password");
			failureHandler.setLoginredirectname("loginRedirect");
			failureHandler.setExceptionmsgname("securityexceptionmsg");
			failureHandler.setDefaultFailureUrl("/spring-admin/login?error=true");
			return failureHandler;
		}
		
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		@Override
		public AuthenticationManager authenticationManager() throws Exception {
			return super.authenticationManager();
		}

	}
}
