package com.shan.app.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private String loginidname;			//로그인 id값이 들어오는 input 태그 name
	private String loginpasswdname;		//로그인 password 값이 들어오는 input 태그 name
	private String loginredirectname;	//로그인 성공 시 redirect 할 URL이 저장되어 있는 input 태그 name
	private String exceptionmsgname;	//예외 메시지를 request의 Attribute에 저장할 때 사용될 key 값
	private String defaultFailureUrl;	//화면에 보여줄 URL(로그인 화면)
	
	public CustomAuthenticationFailureHandler() {
		this.loginidname = "j_username";
		this.loginpasswdname = "j_password";
		this.loginredirectname = "loginRedirect";
		this.exceptionmsgname = "securityexceptionmsg";
		this.defaultFailureUrl = "/login";
	}
	
	public String getLoginidname() {
		return loginidname;
	}

	public void setLoginidname(String loginidname) {
		this.loginidname = loginidname;
	}

	public String getLoginpasswdname() {
		return loginpasswdname;
	}

	public void setLoginpasswdname(String loginpasswdname) {
		this.loginpasswdname = loginpasswdname;
	}

	public String getLoginredirectname() {
		return loginredirectname;
	}

	public void setLoginredirectname(String loginredirectname) {
		this.loginredirectname = loginredirectname;
	}

	public String getExceptionmsgname() {
		return exceptionmsgname;
	}

	public void setExceptionmsgname(String exceptionmsgname) {
		this.exceptionmsgname = exceptionmsgname;
	}

	public String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}


	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		
		//Request 객체의 Attribute에 사용자가 실패 시 입력했던 로그인 ID와 비밀번호를 저장해두어 로그인 페이지에서 이를 접근하도록 한다.
		String loginid = request.getParameter(loginidname);
		String loginpasswd = request.getParameter(loginpasswdname);
		String loginRedirect = request.getParameter(loginredirectname);
		
		request.setAttribute(loginidname, loginid);
		request.setAttribute(loginpasswdname, loginpasswd);
		request.setAttribute(loginredirectname, loginRedirect);
		
		//Request 객체의 Attribute에 예외 메시지 저장
		request.setAttribute(exceptionmsgname, exception.getMessage());
		
		//현재 request는 post상태이기때문에 forward는 defaultFailureUrl이 post로 넘어감.
		//그래서 security http에 antMatchers("defaultFailureUrl")가 등록되어야함.
		//만약 security http formLogin에 loginProcessingUrl 과 defaultFailureUrl이
		//같으면 다시 login 요청을 하는것과 같기때문에 무한루프로 빠지게됨.
		request.getRequestDispatcher(defaultFailureUrl).forward(request, response);
	}

}
