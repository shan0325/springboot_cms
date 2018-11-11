package com.shan.app.repository.admin;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shan.app.domain.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminUserRepositoryTest {

	@Autowired
	private AdminUserRepository adminUserRepository;
	
	@Test
	public void saveUser() {
		User user = new User();
		user.setUserId("admin");
		user.setPassword("1234");
		user.setName("관리자");
		user.setRegDate(new Date());
		
		User newUser = adminUserRepository.save(user);
		
		assertThat(newUser, is(notNullValue()));
	}
	
	
}
