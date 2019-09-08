package com.shan.app.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_manager")
public class Manager {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "manager_id", length = 50)
	private String managerId;

	@Column(nullable = false)
	private String password;
	
	private String salt;
	
	@Size(min = 1, max = 50)
	@Column(length = 100, nullable = false)
	private String name;
	
	private String email;
	
	private String hp;
	
	private String tel;
	
	@Column(name = "reg_manager_id")
	private String regManagerId;
	
	@Column(nullable = false)
	private String state = "N";
	
	@Column(name = "password_update_date")
	private LocalDateTime passwordUpdateDate;
	
	@Column(name = "reg_date")
	private LocalDateTime regDate;
	
	@Column(name = "update_date")
	private LocalDateTime updateDate;
	
	@Column(name = "login_fail_count")
	private Integer loginFailCount;
	
	@Column(name = "refresh_token", length = 1000)
	private String refreshToken;
	
	@Column(name = "refresh_token_reg_date")
	private LocalDateTime refreshTokenRegDate;
	
	@OneToMany(mappedBy = "manager")
	private List<ManagerAuthority> managerAuthoritys = new ArrayList<>();
	
	public void addAuthority(ManagerAuthority authority) {
		this.managerAuthoritys.add(authority);
	}
	
	
}
