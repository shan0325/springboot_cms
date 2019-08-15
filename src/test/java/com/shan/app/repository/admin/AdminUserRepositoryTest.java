package com.shan.app.repository.admin;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.shan.app.domain.User;

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
	public void findUser() {
		User user = adminUserRepository.findByUserId("admin").orElse(null);
		System.out.println("user = " + user);
		
		assertThat(user, is(nullValue()));
	}
	
	
}
