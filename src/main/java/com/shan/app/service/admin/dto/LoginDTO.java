package com.shan.app.service.admin.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

public class LoginDTO {
	
	@Data
	public static class Login {
		@NotBlank(message = "아이디를 입력해주세요.")
		@Size(min = 1, max = 50)
		private String userId;
		
		@NotBlank(message = "비밀번호를 입력해주세요.")
		private String password;
	}
	
	@Data
	public static class LoginToken {
		private String userId;
		private List<AuthorityDTO.Response> authoritys;
		private String token;
		
		public LoginToken(String userId, List<AuthorityDTO.Response> authoritys, String token) {
			this.userId = userId;
			this.authoritys = authoritys;
			this.token = token;
		}
	}

}
