package com.shan.app.error;

public class ExtensionNotAllowedException extends RuntimeException {

	private final String allowExtensions;
	
	public ExtensionNotAllowedException(String allowExtensions) { 
		this(allowExtensions, null);
	}
	
	public ExtensionNotAllowedException(String allowExtensions, Throwable cause) {
		super("The upload extension is " + allowExtensions, cause);
		this.allowExtensions = allowExtensions;
	}
	
	public String getAllowExtensions() {
		return this.allowExtensions;
	}

}
