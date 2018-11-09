package com.shan.app.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_contents_history")
public class ContentsHistory {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "contents_ref_id", nullable = false)
	private Long contentsRefId;
	
	@Column(length = 300, nullable = false)
	private String title;
	
	@Column(name = "site_menu_id", nullable = false)
	private Long siteMenuId;
	
	@Column(name = "use_all_sites_yn", nullable = false)
	private String useAllSitesYn; //모든 사이트에서 사용 가능 여부
	
	@Lob
	private String content;
	
	@Column(name = "use_yn", length = 1, nullable = false)
	private String useYn;
	
	@Column(name = "reg_user_id")
	private String regUserId;
	
	@Column(name = "reg_date")
	private Date regDate;
	
	@Column(name = "update_date")
	private Date updateDate;
}
