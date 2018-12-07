package com.shan.app.service.admin.dto;

import lombok.Data;

public class AuthorityDTO {
	
	
	@Data
	public static class DefaultResponse {
		private String authority;
		private String authorityName;
	}

}
