package com.shan.app.controller.admin;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;
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
import com.shan.app.domain.Category;
import com.shan.app.repository.admin.AdminCategoryRepository;
import com.shan.app.service.admin.AdminCategoryService;
import com.shan.app.service.admin.dto.CategoryDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AdminCategoryResourceTest {

	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	private MockHttpSession session;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Resource(name="adminCategoryRepository")
	private AdminCategoryRepository adminCategoryRepository;
	
	@Resource(name="adminCategoryService")
	private AdminCategoryService adminCategoryService;
	
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
	public void createCategory() throws Exception {
		CategoryDTO.Create create = new CategoryDTO.Create();
		create.setCategory("BRD_CATE");
		create.setCategoryName("게시판 카테고리");
		
		mockMvc.perform(post("/spring-admin/api/category")
							.session(session)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(create)))
				.andDo(print())
				.andExpect(status().isCreated());
	}
	
	@Test
	public void createSubCategory() throws Exception {
		CategoryDTO.Create create = new CategoryDTO.Create();
		create.setCategory("BRD_CATE");
		create.setCategoryName("게시판 카테고리");
		
		Category newCategory = adminCategoryService.createCategory(create);
		
		CategoryDTO.Create subCreate = new CategoryDTO.Create();
		subCreate.setParentId(newCategory.getId());
		subCreate.setCategory("NOTICE");
		subCreate.setCategoryName("공지");
		
		mockMvc.perform(post("/spring-admin/api/category")
							.session(session)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(subCreate)))
				.andDo(print())
				.andExpect(status().isCreated());
	}
	
	@Test
	public void updateCategory() throws Exception {
		
		CategoryDTO.Create create = new CategoryDTO.Create();
		create.setCategory("BRD_CATE");
		create.setCategoryName("게시판 카테고리");
		
		Category newCategory = adminCategoryService.createCategory(create);
		
		CategoryDTO.Update update = new CategoryDTO.Update();
		update.setCategoryName("게시판 카테고리 수정");
		update.setCategoryDesc("게시판 카테고리 입니다.");
		update.setOrd(1);
		update.setUseYn("Y");
		
		mockMvc.perform(put("/spring-admin/api/category/" + newCategory.getId())
							.session(session)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(update)))
				.andDo(print())
				.andExpect(status().isOk());
		
	}
	
	@Test
	public void getCategorys() throws Exception {
		
		createSubCategory();
		
		mockMvc.perform(get("/spring-admin/api/categorys")
						.session(session)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	public void getCategory() throws Exception {
		
		CategoryDTO.Create create = new CategoryDTO.Create();
		create.setCategory("BRD_CATE");
		create.setCategoryName("게시판 카테고리");
		
		Category newCategory = adminCategoryService.createCategory(create);
		
		mockMvc.perform(get("/spring-admin/api/category/" + newCategory.getId())
						.session(session)
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk());
	}
}
