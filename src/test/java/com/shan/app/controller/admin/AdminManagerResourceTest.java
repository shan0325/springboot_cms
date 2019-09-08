package com.shan.app.controller.admin;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import com.shan.app.service.admin.dto.ManagerDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AdminManagerResourceTest {

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
	public void createManagerTest() throws Exception {
		ManagerDTO.Create manager = new ManagerDTO.Create();
		manager.setManagerId("admin");
		manager.setPassword("1234");
		manager.setName("관리자");
		manager.setState("N");

		List<String> authoritys = new ArrayList<>();
		authoritys.add("ADMIN");
		manager.setAuthoritys(authoritys);
		
	    mockMvc.perform(post("/spring-admin/api/managers")
				            .header("Authorization", "Bearer " + accessToken)
				            .contentType(MediaType.APPLICATION_JSON_UTF8)
				            .content(objectMapper.writeValueAsString(manager)))
	    		.andDo(print())
	            .andExpect(status().isCreated());
	}
	
	@Test
	public void updateManagerTest() throws Exception {
		ManagerDTO.Update update = new ManagerDTO.Update();
		update.setName("관리자");
		update.setEmail("admin@naver.com");
		update.setHp("01011112222");
		update.setTel("023334444");
		update.setState("Y");

		List<String> authoritys = new ArrayList<>();
		authoritys.add("ADMIN");
		authoritys.add("MEMBER");
		
		update.setAuthoritys(authoritys);
		
		mockMvc.perform(put("/spring-admin/api/managers/93")
							.header("Authorization", "Bearer " + accessToken)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(update)))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	public void getManagerTest() throws Exception {
		mockMvc.perform(get("/spring-admin/api/managers/93")
							.header("Authorization", "Bearer " + accessToken))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	public void getManagersTest() throws Exception {
		mockMvc.perform(get("/spring-admin/api/managers")
							.header("Authorization", "Bearer " + accessToken))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	public void deleteManagerTest() throws Exception {
		mockMvc.perform(delete("/spring-admin/api/managers/93")
							.header("Authorization", "Bearer " + accessToken))
				.andDo(print())
				.andExpect(status().isOk());
	}
}
