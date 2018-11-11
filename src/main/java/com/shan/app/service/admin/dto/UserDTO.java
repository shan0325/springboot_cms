package com.shan.app.service.admin.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.shan.app.domain.Authority;

import lombok.Data;

public class UserDTO {

	@Data
	public static class Create {
		@NotBlank(message = "아이디를 입력해주세요.")
		@Size(min = 1, max = 50)
		private String userId;
		
		@NotBlank(message = "비밀번호를 입력해주세요.")
		private String password;
		
		@NotBlank(message = "이름을 입력해주세요.")
		private String name;
		
		private String email;
		private String hp;
		private String tel;
		
		@NotBlank(message = "권한을 입력해주세요.")
		private List<String> authorities;
	}
	
	public static class Response {
		private Long id;
		private String userId;
		private String name;
		private String email;
		private String hp;
		private String tel;
		private String regUserId;
		private String state;
		private Date passwordUpdateDate;
		private Date regDate;
		private Date updateDate;
		private List<Authority> authorities;
	}
}
