package com.shan.app.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_user")
public class User {
	
	@NotBlank
	@Size(min = 1, max = 50)
	@Id
	@Column(name = "user_id", length = 50)
	private String userId;

	@NotBlank
	@Column(nullable = false)
	private String password;
	
	private String salt;
	
	@NotBlank
	@Size(min = 1, max = 50)
	@Column(length = 100, nullable = false)
	private String name;
	
	private String email;
	
	private String hp;
	
	private String tel;
	
	@Column(name = "reg_user_id")
	private String regUserId;
	
	@Column(nullable = false)
	private String state = "N";
	
	@Column(name = "password_update_date")
	private Date passwordUpdateDate;
	
	@Column(name = "reg_date")
	private Date regDate;
	
	@Column(name = "update_date")
	private Date updateDate;
	
	@Column(name = "login_fail_count")
	private Integer loginFailCount;
	
	@OneToMany(mappedBy = "user")
	private List<UserAuthority> userAuthoritys = new ArrayList<>();
}
