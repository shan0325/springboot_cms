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
	
	public static class Response extends ResourceSupport {
		private Long id;
		private String authority;
		private String authorityName;
		
		public Long getAuthorityId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getAuthority() {
			return authority;
		}
		public void setAuthority(String authority) {
			this.authority = authority;
		}
		public String getAuthorityName() {
			return authorityName;
		}
		public void setAuthorityName(String authorityName) {
			this.authorityName = authorityName;
		}
		
	}

}
