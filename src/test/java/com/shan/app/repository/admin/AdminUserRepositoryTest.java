package com.shan.app.repository.admin;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import javax.jdo.annotations.Transactional;
import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.shan.app.domain.User;
import com.shan.app.domain.UserAuthority;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminUserRepositoryTest {

	@Autowired
	private AdminUserRepository adminUserRepository;
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EntityManager em;
	
	@Test
	public void saveUser() {
		User user = new User();
		user.setUserId("admin");
		user.setPassword(bCryptPasswordEncoder.encode("1234"));
		user.setName("관리자");
		user.setRegDate(LocalDateTime.now());
		
		User newUser = adminUserRepository.save(user);
		
		assertThat(newUser, is(notNullValue()));
	}
	
	@Test
	@Transactional
	public void findUser() {
		User user = adminUserRepository.findOneByUserId("admin");
		System.out.println("user = " + user.getUserAuthoritys());
		
		em.persist(user);
		
		assertThat(user.getUserAuthoritys(), is(notNullValue()));
		
		List<UserAuthority> userAuthoritys = user.getUserAuthoritys();
		userAuthoritys.forEach(userAuthority -> {
			System.out.println("userAuthority = " + userAuthority.getAuthority().getAuthority());
			System.out.println("userAuthority = " + userAuthority.getUser().getName());
		});
	}
	
	
}
