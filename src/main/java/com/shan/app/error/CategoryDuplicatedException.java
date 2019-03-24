package com.shan.app.error;

public class CategoryDuplicatedException extends RuntimeException {

	private String category;
	
	public CategoryDuplicatedException(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return this.category;
	}
}
