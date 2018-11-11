package com.shan.app.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_menu")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Menu {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "parent_id", nullable = false)
	private Long parentId;
	
	@Column(name = "top_id", nullable = false)
	private Long topId;

	@Column(nullable = false)
	private Integer level; //level이 1이면 홈페이지
	
	@Column(length = 100, nullable = false)
	private String name;
	
	private String description;
	
	@Column(name = "image_menu_path")
	private String imageMenuPath;
	
	@Column(name = "use_yn", length = 1, nullable = false)
	private String useYn;
	
	@Column(name = "site_gubun", length = 50, nullable = false)
	private String siteGubun; //all, admin, home 구분
	
	@Column(name = "menu_type", length = 50, nullable = false)
	private String menuType; //list:목록, url:링크, board:게시판, contents:컨텐츠
	
	private String url;
	
	@Column(name = "url_target", length = 50)
	private String urlTarget;
	
	@Column(nullable = false)
	private Integer ord;
	
	@Column(name = "reg_date")
	private Date regDate;
	
	@Column(name = "update_date")
	private Date updateDate;
	
	@ManyToOne
	@JoinColumn(name = "board_manager_id")
	private BoardManager boardManager;
	
	@ManyToOne
	@JoinColumn(name = "contents_id")
	private Contents contents;
}
