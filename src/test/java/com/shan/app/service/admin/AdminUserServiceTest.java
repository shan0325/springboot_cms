package com.shan.app.service.admin;

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
		user.setUserId("admin");
		user.setPassword("1234");
		user.setName("관리자");
		user.setEmail("admin@naver.com");
		user.setHp("010-1111-2222");
		user.setState("Y");

		List<String> authoritys = new ArrayList<>();
		authoritys.add("ADMIN");
		
		user.setAuthoritys(authoritys);
		
//		User newUser = adminUserService.createUser("", user);
//		assertThat(user.getUserId(), is(newUser.getUserId()));
	}

}
