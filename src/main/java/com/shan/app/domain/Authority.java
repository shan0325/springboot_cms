package com.shan.app.domain;

import java.util.ArrayList;
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
@Table(name = "tb_authority")
public class Authority {

	@NotBlank
	@Size(max = 50)
	@Id
	@Column(length = 50)
	private String authority;
	
	@NotBlank
	@Column(name = "authority_name", nullable = false, length = 200)
	private String authorityName;
	
	@OneToMany(mappedBy = "authority")
	private List<UserAuthority> userAuthoritys = new ArrayList<>();
}
