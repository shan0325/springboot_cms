package com.shan.app.service.admin.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

public class CategoryDTO {

	@Data
	public static class Create {
		@NotBlank(message = "카테고리를 입력해주세요.")
		private String category;
		@NotBlank(message = "카테고리명을 입력해주세요.")
		private String categoryName;
		private String parentCategory;
		private Long parentId;
		private String categoryDesc;
		private Integer ord;
	}
	
	@Data
	public static class Response {
		private Long id;
		private String category;
		private String categoryName;
		private String parentCategory;
		private String topCategory;
		private String categoryDesc;
		private Integer level;
		private String useYn;
		private Integer ord;
		
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
		private LocalDateTime regDate;
		
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
		private LocalDateTime updateDate;
	}
	
	@Data
	public static class Update {
		@NotBlank(message = "카테고리명을 입력해주세요.")
		private String categoryName;
		private String categoryDesc;
		@NotBlank(message = "사용여부를 입력해주세요.")
		private String useYn;
		@NotNull(message = "순서를 입력해주세요.")
		private Integer ord;
	}
}
