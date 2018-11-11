package com.shan.app.service.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shan.app.domain.User;
import com.shan.app.service.admin.dto.UserDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminUserServiceTest {

	@Resource(name="adminUserService")
	private AdminUserService adminUserService;
	
	@Test
	public void createUserTest() {
		UserDTO.Create user = new UserDTO.Create();
		user.setUserId("test");
		user.setPassword("1234");
		user.setName("테스트");

		List<String> authorities = new ArrayList<>();
		authorities.add("USER");
		
		user.setAuthorities(authorities);
		
		User newUser = adminUserService.createUser(user);
		assertThat(user.getUserId(), is(newUser.getUserId()));
	}

}
