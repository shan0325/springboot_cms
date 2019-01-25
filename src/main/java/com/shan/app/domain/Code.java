package com.shan.app.domain;

import java.time.LocalDateTime;
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
@Table(name = "tb_code")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Code {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length = 20)
	private String code;
	
	@Column(length = 100, nullable = false)
	private String codeName;
	
	@Column(length = 20)
	private String parentCode;
	
	@Column(length = 20)
	private String topCode;
	
	@Column(length = 200)
	private String codeDesc;
	
	@Column
	private Integer level;
	
	@Column(length = 1, nullable = false)
	private String useYn;
	
	@Column(nullable = false)
	private Integer ord;
	
	@Column(name = "reg_date")
	private LocalDateTime regDate;
	
	@Column(name = "update_date")
	private LocalDateTime updateDate;
	
	
}
