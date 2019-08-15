package com.shan.app.util;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class AccessTokenUtil {

	public static String deleteBearerFromAuthorization(String authorization) {
		return authorization.replace(OAuth2AccessToken.BEARER_TYPE + " ", "");
	}
}
