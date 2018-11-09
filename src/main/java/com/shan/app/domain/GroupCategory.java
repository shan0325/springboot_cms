package com.shan.app.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_group_category")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class GroupCategory {
	
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "group_category", unique = true, nullable = false, length = 20)
	private String groupCategory;
	
	@Column(name = "group_category_name", nullable = false, length = 100)
	private String groupCategoryName;
	
	@Column(length = 1, nullable = false)
	private String useYn;
	
	@Column(name = "reg_date")
	private Date regDate;
	
	@Column(name = "update_date")
	private Date updateDate;
	
	@OneToMany(mappedBy = "groupCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Category> categorys;
}
