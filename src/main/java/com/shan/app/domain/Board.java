package com.shan.app.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_board")
public class Board {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "parent_id", nullable = false)
	private Long parentId;
	
	@Column(name = "top_id", nullable = false)
	private Long topId;
	
	@Column(nullable = false)
	private Integer depth;
	
	@Column(length = 300, nullable = false)
	private String title;
	
	@Lob
	private String content;
	
	@Column(nullable = false)
	private String useYn;
	
	private String category;
	
	@Column(name = "notice_yn", length = 1)
	private String noticeYn;
	
	@Column(name = "start_date")
	private String startDate;
	
	@Column(name = "end_date")
	private String endDate;
	
	@Column(name = "secret_yn", length = 1)
	private String secretYn;
	
	private String phone;
	
	private String email;
	
	private Integer ord;
	
	@Column(name = "view_count")
	private String viewCount;
	
	private String password;
	
	@Column(name = "reg_user_id")
	private String regUserId;
	
	@Column(name = "reg_user_name")
	private String regUserName;
	
	@Column(name = "reg_date")
	private Date regDate;
	
	@Column(name = "update_date")
	private Date updateDate;
	
	@ManyToOne
	@JoinColumn(name = "board_manager_id", nullable = false)
	private BoardManager boardManager;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "refer_id")
	private List<File> files;
}
