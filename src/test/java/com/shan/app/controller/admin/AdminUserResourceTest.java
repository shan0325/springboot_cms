package com.shan.app.controller.admin;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.shan.app.domain.User;
import com.shan.app.repository.admin.AdminUserRepository;
import com.shan.app.service.admin.dto.UserDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminUserResourceTest {

	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	private MockHttpSession session;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private AdminUserRepository adminUserRepository;

	@Before
	public void setUp() throws Exception {
		//관리자 등록
		User user = new User();
		user.setUserId("admin");
		user.setPassword("1234");
		user.setName("관리자");
		user.setRegDate(new Date());
		adminUserRepository.save(user);
		
		
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
										.addFilter(springSecurityFilterChain)
										.build();
		
		this.session = (MockHttpSession) mockMvc.perform(formLogin("/spring/login")
															.user("userId", "admin")
															.password("password", "1234"))
												.andExpect(status().is3xxRedirection())
												.andReturn().getRequest().getSession();
	}
	
	@Test
	public void createUserTest() throws Exception {
		UserDTO.Create user = new UserDTO.Create();
		user.setUserId("test");
		user.setPassword("1234");
		user.setName("테스트");

		List<String> authorities = new ArrayList<>();
		authorities.add("USER");
		
		user.setAuthorities(authorities);
		
		mockMvc.perform(post("/spring/user")
							.session(session)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(user)))
				.andDo(print())
				.andExpect(status().isCreated());
	}
}
