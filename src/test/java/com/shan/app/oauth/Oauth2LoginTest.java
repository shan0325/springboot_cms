package com.shan.app.oauth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class Oauth2LoginTest {
	
	@Autowired
    private WebApplicationContext wac;
 
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
 
    private MockMvc mockMvc;
	
	
	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
										.addFilter(springSecurityFilterChain)
										.build();
	}

	public static String obtainAccessToken(MockMvc mockMvc, String username, String password) throws Exception {
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type", "password");
//	    params.add("client_id", "spring");
	    params.add("username", username);
	    params.add("password", password);
	 
	    ResultActions result = mockMvc.perform(post("/oauth/token")
								        .params(params)
								        .with(httpBasic("spring", "1234"))
								        .accept(MediaType.APPLICATION_JSON_UTF8))
								      	.andDo(print())
								        .andExpect(status().isOk())
								        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	 
	    String resultString = result.andReturn().getResponse().getContentAsString();
	 
	    JacksonJsonParser jsonParser = new JacksonJsonParser();
	    return jsonParser.parseMap(resultString).get("access_token").toString();
	}
	
	public static String obtainAccessToken(MockMvc mockMvc) throws Exception {
		return obtainAccessToken(mockMvc, "admin", "1234");
	}
	
	@Test
	public void when_callUsers_expect_success() throws Exception {
	    String accessToken = obtainAccessToken(mockMvc);
	    
	    mockMvc.perform(get("/spring-admin/api/users")
	            .header("Authorization", "Bearer " + accessToken)
	            .accept(MediaType.APPLICATION_JSON_UTF8))
	    		.andDo(print())
	            .andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
}
