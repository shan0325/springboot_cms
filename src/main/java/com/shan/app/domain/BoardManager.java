package com.shan.app.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_board_manager")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BoardManager {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length = 300, nullable = false)
	private String name;
	
	@Column(name = "site_menu_id", nullable = false)
	private Long siteMenuId;
	
	@Column(name = "use_all_sites_yn", nullable = false)
	private String useAllSitesYn; //모든 사이트에서 사용 가능 여부
	
	private String description;
	
	@Column(name = "skin_code", length = 20, nullable = false)
	private String skinCode;
	
	@Column(name = "use_yn", length = 1, nullable = false)
	private String useYn;
	
	@Column(name = "list_count")
	private Integer listCount; //리스트 갯수

	@Column(name = "attach_file_count")
	private Integer attachFileCount; //첨부파일 갯수
	
	@Column(name = "write_use_yn", length = 1)
	private String writeUseYn; //게시물 글쓰기, 수정 사용여부
	
	@Column(name = "delete_use_yn", length = 1)
	private String deleteUseYn; //게시물 삭제 사용여부
	
	@Column(name = "reply_use_yn", length = 1)
	private String replyUseYn; //답글 사용여부
	
	@Column(name = "commont_use_yn", length = 1)
	private String commentUseYn; //댓글 사용여부
	
	@Column(name = "notice_use_yn", length = 1)
	private String noticeUseYn; //공지 사용여부
	
	@Column(name = "ord_use_yn", length = 1)
	private String ordUseYn; //정렬필드 사용여부
	
	@Column(name = "writer_use_yn", length = 1)
	private String writerUseYn; //작성자 사용여부
	
	@Column(name = "phone_use_yn", length = 1)
	private String phoneUseYn; //연락처 사용여부
	
	@Column(name = "email_use_yn", length = 1)
	private String emailUseYn; //이메일 사용여부
	
	@Column(name = "start_date_use_yn", length = 1)
	private String startDateUseYn; //게시 시작일 사용여부
	
	@Column(name = "end_date_use_yn", length = 1)
	private String endDateUseYn; //게시 종료일 사용여부
	
	@Column(name = "secret_use_yn", length = 1)
	private String secretUseYn; //비밀글 사용여부
	
	@Column(name = "category_use_yn", length = 1)
	private String categoryUseYn; //카테고리 사용여부
	
	@Column(name = "group_category")
	private String groupCategory; //카테고리

	@Column(name = "thumb_width")
	private String thumbWidth; //썸네일 가로길이
	
	@Column(name = "thumb_height")
	private String thumbHeight; //썸네일 세로길이
	
	@Column(name = "reg_date")
	private Date regDate;
	
	@Column(name = "update_date")
	private Date updateDate;
}
