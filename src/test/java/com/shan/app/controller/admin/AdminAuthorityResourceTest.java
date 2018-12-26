package com.shan.app.controller.admin;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.shan.app.service.admin.dto.AuthorityDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AdminAuthorityResourceTest {

	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	private MockHttpSession session;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
										.addFilter(springSecurityFilterChain)
										.build();
		
		this.session = (MockHttpSession) mockMvc.perform(formLogin("/spring-admin/login")
													.user("userId", "admin")
													.password("password", "1234"))
												.andExpect(status().is3xxRedirection())
												.andReturn().getRequest().getSession();
	}
	
	@Test
	public void createAuthorityTest() throws Exception {
		AuthorityDTO.Create create = new AuthorityDTO.Create();
		create.setAuthority("MANAGER");
		create.setAuthorityName("매니저");
		
		mockMvc.perform(post("/spring-admin/api/authority")
							.session(session)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(create)))
				.andDo(print())
				.andExpect(status().isCreated());
	}
	
	@Test
	public void updateAuthorityTest() throws Exception {
		AuthorityDTO.Update update = new AuthorityDTO.Update();
		update.setAuthorityName("회원2");
		
		mockMvc.perform(put("/spring-admin/api/authority/MEMBER")
							.session(session)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(update)))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
}
