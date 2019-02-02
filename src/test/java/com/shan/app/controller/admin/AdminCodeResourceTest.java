package com.shan.app.controller.admin;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.shan.app.service.admin.dto.CodeDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AdminCodeResourceTest {

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
	public void createCode() throws Exception {
		CodeDTO.Create create = new CodeDTO.Create();
		create.setCode("EMAIL");
		create.setCodeName("이메일");
		create.setCodeDesc("이메일 코드");
		
		mockMvc.perform(post("/spring-admin/api/code")
							.session(session)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(create)))
				.andDo(print())
				.andExpect(status().isCreated());
	}
	
	@Test
	public void createSubCode() throws Exception {
		CodeDTO.Create create = new CodeDTO.Create();
		create.setParentCode("EMAIL");
		create.setCode("EMAIL_NAVER");
		create.setCodeName("네이버");
		create.setCodeDesc("naver.com");
		
		mockMvc.perform(post("/spring-admin/api/code")
							.session(session)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(create)))
				.andDo(print())
				.andExpect(status().isCreated());
	}
	
	@Test
	public void updateCode() throws Exception {
		CodeDTO.Update update = new CodeDTO.Update();
		update.setCodeName("이메일코드2");
		update.setCodeDesc("이메일코드 입니다.");
		update.setUseYn("N");
		update.setOrd(2);
		
		mockMvc.perform(put("/spring-admin/api/code/37")
						.session(session)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(update)))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	public void getCodes() throws Exception {
		mockMvc.perform(get("/spring-admin/api/codes")
						.session(session))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	public void getCode() throws Exception {
		mockMvc.perform(get("/spring-admin/api/code/37")
						.session(session))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	public void deleteCode() throws Exception {
		mockMvc.perform(delete("/spring-admin/api/code/37")
						.session(session))
				.andDo(print())
				.andExpect(status().isOk());
	}
}
