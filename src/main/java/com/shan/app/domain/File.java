package com.shan.app.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_file")
public class File {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "refer_id")
	private Long referId;
	
	@Column(name = "original_file_name", nullable = false) //original 파일명
	private String originalFileName;
	
	@Column(name = "new_file_name", nullable = false) //new 파일명
	private String newFileName;
	
	@Column(name = "save_path", nullable = false)
	private String savePath;
	
	@Column(nullable = false)
	private Long size;
	
	@Column(name = "table_name")
	private String tableName;
	
	private String gubun;
	
	@Column(name = "reg_date")
	private Date regDate;
	
	@Column(name = "update_date")
	private Date updateDate;
	
}
