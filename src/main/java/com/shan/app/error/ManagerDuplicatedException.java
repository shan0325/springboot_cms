package com.shan.app.error;

public class ManagerDuplicatedException extends RuntimeException {
	
	private String managerId;
	
	public ManagerDuplicatedException(String managerId) {
		this.managerId = managerId;
    }
	
	public String getManagerId() {
		return managerId;
	}
}
