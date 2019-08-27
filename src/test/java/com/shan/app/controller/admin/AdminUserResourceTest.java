package com.shan.app.controller.admin;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shan.app.oauth.Oauth2LoginTest;
import com.shan.app.service.admin.dto.LoginDTO;
import com.shan.app.service.admin.dto.UserDTO;
import com.shan.app.service.admin.dto.UserDTO.Update;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AdminUserResourceTest {

	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	private MockHttpSession session;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private String accessToken;
	
	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
										.addFilter(springSecurityFilterChain)
										.build();
		
//		this.session = (MockHttpSession) mockMvc.perform(formLogin("/spring-admin/login")
//													.user("userId", "admin")
//													.password("password", "1234"))
//												.andExpect(status().is3xxRedirection())
//												.andReturn().getRequest().getSession();
		
		accessToken = Oauth2LoginTest.obtainAccessToken(mockMvc);
		System.out.println("accessToken : " + accessToken);
	}
	
	@Test
	public void loginTest() throws Exception {
		assertThat(this.session.getId(), is("1"));
	}
	
	@Test
	public void createUserTest() throws Exception {
		UserDTO.Create user = new UserDTO.Create();
		user.setUserId("test2");
		user.setPassword("1234");
		user.setName("테스트");
		user.setState("N");

		List<String> authoritys = new ArrayList<>();
		authoritys.add("ADMIN");
		authoritys.add("MEMBER");
		user.setAuthoritys(authoritys);
		
	    mockMvc.perform(post("/spring-admin/api/users")
				            .header("Authorization", "Bearer " + accessToken)
				            .contentType(MediaType.APPLICATION_JSON_UTF8)
				            .content(objectMapper.writeValueAsString(user)))
	    		.andDo(print())
	            .andExpect(status().isCreated());
		
//		mockMvc.perform(post("/spring-admin/api/users")
//							.session(session)
//							.contentType(MediaType.APPLICATION_JSON)
//							.content(objectMapper.writeValueAsString(user)))
//				.andDo(print())
//				.andExpect(status().isCreated());
	}
	
	@Test
	public void updateUserTest() throws Exception {
		Update update = new UserDTO.Update();
		update.setName("관리자");
		update.setEmail("admin@naver.com");
		update.setHp("01011112222");
		update.setTel("023334444");
		update.setState("Y");

		List<String> authoritys = new ArrayList<>();
		authoritys.add("ADMIN");
		authoritys.add("MEMBER");
		
		update.setAuthoritys(authoritys);
		
		mockMvc.perform(put("/spring-admin/api/users/69")
							.header("Authorization", "Bearer " + accessToken)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(update)))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	public void getUserTest() throws Exception {
		mockMvc.perform(get("/spring-admin/api/users/69")
							.header("Authorization", "Bearer " + accessToken))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	public void getUsersTest() throws Exception {
		mockMvc.perform(get("/spring-admin/api/users")
							.header("Authorization", "Bearer " + accessToken))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	public void deleteUserTest() throws Exception {
		mockMvc.perform(delete("/spring-admin/api/users/69")
							.header("Authorization", "Bearer " + accessToken))
				.andDo(print())
				.andExpect(status().isOk());
	}
}
