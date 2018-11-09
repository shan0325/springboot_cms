package com.shan.app.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

/**
 * 로그인 성공 후 부가적인 작업 실행 클래스
 * 로그인 작업이 성공했을 때 먼가 부가적인 작업을 하고 싶을 경우 Spring Security에서 제공하는 인터페이스인
 * org.springframework.security.web.authentication.AuthenticationSuccessHandler를 구현한 클래스를 만든뒤에 
 * 이 클래스를 Spring Bean으로 등록한다. 그런 후 <form-login> 태그의 authentication-success-handler-ref 속성에
 * 해당 클래스의 id를 넣어주면 된다.
 * 
 * 여기서는 로그인 할 때 로그인 후 이동할 URL을 지정하면 해당 URL로 이동하는 기능을 넣었다.
 * 순서는
 * 1. 지정된 Request Parameter(loginRedirect)에 로그인 작업을 마친 뒤 redirect할 URL을 지정했다면 이 URL로 redirect 하도록 한다.
 * 2. 만약 지정된 Request Parameter에 지정된 URL이 없다면 세션에 저장된 URL로 redirect 하도록 한다.
 * 3. 세션에 저장된 URL도 없다면 Request의 REFERER 헤더값을 읽어서 로그인 페이지를 방문하기 전 페이지의 URL을 읽어서 거기로 이동하도록 한다.
 * 4. 위의 3가지 경우 모두 만족하는게 없으면 이 클래스에 있는 defaultURL 속성에 지정된 URL로 이동하도록 한다.
 * 
 * @author shan
 *
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
	
	private RequestCache requestCache = new HttpSessionRequestCache();
	private String targetUrlParameter;
	private String defaultUrl;
	private boolean useReferer;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	public CustomAuthenticationSuccessHandler() {
		targetUrlParameter = "";
		defaultUrl = "/";
		useReferer = false;
	}
	
	public String getTargetUrlParameter() {
		
		return targetUrlParameter;
	}
	
	public void setTargetUrlParameter(String targetUrlParameter) {
		this.targetUrlParameter = targetUrlParameter;
	}
	
	public String getDefaultUrl() {
		
		return defaultUrl;
	}
	
	public void setDefaultUrl(String defaultUrl) {
		
		this.defaultUrl = defaultUrl;
	}
	
	public boolean isUserReferer() {
		
		return useReferer;
	}
	
	public void setUseReferer(boolean useReferer) {
		
		this.useReferer = useReferer;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		clearAuthenticationAttributes(request);
		
		int intRedirectStrategy = decideRedirectStrategy(request, response);
		
		switch(intRedirectStrategy) {
		case 1:
			useTargetUrl(request, response);
			break;
		case 2:
			useSessionUrl(request, response);
			break;
		case 3:
			useRefererUrl(request, response);
			break;
		default :
			useDefaultUrl(request, response);
		}
	}
	
	private void clearAuthenticationAttributes(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			return;
		}
		
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
	
	private void useTargetUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if(savedRequest != null) {
			requestCache.removeRequest(request, response);
		}
		String targetUrl = request.getParameter(targetUrlParameter);
		logger.debug("targetUrl = " + targetUrl);
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	private void useSessionUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		String targetUrl = savedRequest.getRedirectUrl();
		logger.debug("sessionUrl = " + targetUrl);
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	private void useRefererUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String targetUrl = request.getHeader("REFERER");
		logger.debug("refererUrl = " + targetUrl);
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	private void useDefaultUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		logger.debug("defaultUrl = " + defaultUrl);
		redirectStrategy.sendRedirect(request, response, defaultUrl);
	}
	
	/**
	 * 인증 성공 후 어떤 URL로 redirect 할지를 결정한다.
	 * 판단 기준은 targetUrlParameter 값을 읽은 URL이 존재할 경우 그것을 1순위
	 * 1순위 URL이 없을 경우 Spring Security가 세션에 저장한 URL을 2순위
	 * 2순위 URL이 없을 경우 Request의 REFERER를 사용하고 그 REFERER URL이 존재할 경우 그 URL을 3순위
	 * 3순위 URL이 없을 경우 Default URL을 4순위로 한다.
	 * @param request
	 * @param response
	 * @return
	 */
	private int decideRedirectStrategy(HttpServletRequest request, HttpServletResponse response) {
		
		int result = 0;
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		if(!"".equals(targetUrlParameter)) {
			String targetUrl = request.getParameter(targetUrlParameter);
			
			if(StringUtils.hasText(targetUrl)) {
				result = 1;
			} else {
				
				if(savedRequest != null) {
					result = 2;
				} else {
					String refererUrl = request.getHeader("REFERER");
					
					if(useReferer && StringUtils.hasText(refererUrl)) {
						result = 3;
					} else {
						result = 0;
					}
				}				
			}
			
			return result;
		}
		
		if(savedRequest != null) {
			
			result = 2;
			return result;
		}
		
		String refererUrl = request.getHeader("REFERER");
		
		if(useReferer && StringUtils.hasText(refererUrl)) {
			
			result = 3;
		} else {
			
			result = 0;
		}
		
		return result;
	}
	
}
