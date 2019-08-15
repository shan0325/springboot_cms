package com.shan.app.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "authority")
//@ToString(exclude = {"userAuthoritys"})
@Getter
@Setter
@Entity
@Table(name = "tb_authority")
public class Authority {
	
	@Id
	@GeneratedValue
	private Long id;

	@Column(length = 50)
	private String authority;
	
	@Column(name = "authority_name", nullable = false, length = 200)
	private String authorityName;
	
//	@OneToMany(mappedBy = "authority")
//	private List<UserAuthority> userAuthoritys = new ArrayList<>();
}
