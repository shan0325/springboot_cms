package com.shan.app.error;

public class CodeDuplicatedException extends RuntimeException {

	private String code;
	
	public CodeDuplicatedException(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
}
