package com.shan.app.service.admin.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.hateoas.ResourceSupport;

import lombok.Data;

public class AuthorityDTO {
	
	@Data
	public static class Create {
		@NotBlank(message = "권한을 입력해주세요.")
		private String authority;
		@NotBlank(message = "권한명을 입력해주세요.")
		private String authorityName;
	}
	
	@Data
	public static class Update {
		@NotBlank(message = "권한명을 입력해주세요.")
		private String authorityName;
	}
	
	@Data
	public static class Response extends ResourceSupport {
		private String authority;
		private String authorityName;
	}

}
