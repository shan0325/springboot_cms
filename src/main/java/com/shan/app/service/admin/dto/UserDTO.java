package com.shan.app.service.admin.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFormat;

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
		
		@Email(message = "이메일을 정확히 입력해주세요.")
		private String email;
		
		@Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
		private String hp;
		
		@Pattern(regexp = "^[0-9]*$", message = "숫자만 입력가능합니다")
		private String tel;
		
		@NotNull(message = "상태를 선택해주세요.")
		private String state;
		
		@NotNull(message = "권한을 입력해주세요.")
		private List<String> authoritys;
	}
	
	@Data
	public static class Update {
		private String password;
		private String passwordConfirm;
		
		@NotBlank(message = "이름을 입력해주세요.")
		private String name;
		
		@Email(message = "이메일을 정확히 입력해주세요.")
		private String email;
		
		@Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
		private String hp;
		
		@Pattern(regexp = "^[0-9]*$", message = "숫자만 입력가능합니다")
		private String tel;
		
		@NotNull(message = "상태를 선택해주세요.")
		private String state;
		
		@NotNull(message = "권한을 입력해주세요.")
		private List<String> authoritys;
	}
	
	@Data
	public static class Response extends ResourceSupport {
		private String userId;
		private String name;
		private String email;
		private String hp;
		private String tel;
		private String regUserId;
		private String state;
		
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
		private LocalDateTime passwordUpdateDate;
		
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
		private LocalDateTime regDate;
		
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
		private LocalDateTime updateDate;
		
		private List<AuthorityDTO.Response> authoritys;
	}
}
