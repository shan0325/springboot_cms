package com.shan.app.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import lombok.ToString;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
//Entity 클래스를 프로젝트 코드상에서 기본생성자로 생성하는 것은 막되, JPA에서 Entity 클래스를 생성하는것은 허용하기 위해 추가
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@ToString(exclude = {"userAuthoritys"})
@Getter
@Setter
@Entity
@Table(name = "tb_user")
public class User {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "user_id", length = 50)
	private String userId;

	@Column(nullable = false)
	private String password;
	
	private String salt;
	
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
	
//	@OneToMany(mappedBy = "user")
//	private List<UserAuthority> userAuthoritys = new ArrayList<>();
//	
//	public void addAuthority(UserAuthority authority) {
//		this.userAuthoritys.add(authority);
//	}
	
	
}
