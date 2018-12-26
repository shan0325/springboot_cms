package com.shan.app.error;

public class AuthorityDuplicatedException extends RuntimeException {

	private String authority;
	
	public AuthorityDuplicatedException(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return authority;
	}
	
}
