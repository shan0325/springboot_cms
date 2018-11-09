package com.shan.app.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class CustomRoleAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final Logger logger = LoggerFactory.getLogger(CustomRoleAuthenticationSuccessHandler.class);
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.info("Can't redirect");
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        String url="";

        Collection<? extends GrantedAuthority> authorities =  authentication.getAuthorities();

        List<String> roles = new ArrayList<String>();

        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }

        if (isStaff(roles)) {
            url = "/staff";
        } else if (isAdmin(roles)) {
            url = "/admin";
        } else if (isStudent(roles)) {
            url = "/student";
        }else if (isUser(roles)) {
            url = "/home";
        } else {
            url="/Access_Denied";
        }

        return url;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    private boolean isUser(List<String> roles) {
        if (roles.contains("ROLE_USER")) {
            return true;
        }
        return false;
    }

    private boolean isStudent(List<String> roles) {
        if (roles.contains("ROLE_Student")) {
            return true;
        }
        return false;
    }

    private boolean isAdmin(List<String> roles) {
        if (roles.contains("ROLE_SystemAdmin") || roles.contains("ROLE_ExaminationsStaff")) {
            return true;
        }
        return false;
    }

    private boolean isStaff(List<String> roles) {
        if (roles.contains("ROLE_AcademicStaff") || roles.contains("ROLE_UniversityAdmin")) {
            return true;
        }
        return false;
    }
}
