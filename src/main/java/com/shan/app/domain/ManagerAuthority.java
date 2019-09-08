package com.shan.app.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_manager_authority")
@IdClass(ManagerAuthorityId.class)
public class ManagerAuthority {

	@Id
	@ManyToOne
	@JoinColumn(name = "manager_id")
	private Manager manager;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "authority_id")
	private Authority authority;
	
}
