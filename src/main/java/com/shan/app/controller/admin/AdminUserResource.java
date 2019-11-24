package com.shan.app.controller.admin;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shan.app.domain.User;
import com.shan.app.domain.UserAuthority;
import com.shan.app.service.admin.AdminUserAuthorityService;
import com.shan.app.service.admin.AdminUserService;
import com.shan.app.service.admin.dto.AuthorityDTO;
import com.shan.app.service.admin.dto.UserDTO;
import com.shan.app.service.admin.dto.UserDTO.Response;

@RestController
@RequestMapping("/spring-admin/api")
public class AdminUserResource {
	
	private final Logger logger = LoggerFactory.getLogger(AdminUserResource.class);

	@Resource(name="adminUserService")
	private AdminUserService adminUserService;
	
	@Resource(name="adminUserAuthorityService")
	private AdminUserAuthorityService adminUserAuthorityService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody @Valid UserDTO.Create create) {
		logger.info("Request Param [{}, {}]", authorization, create);
		
		UserDTO.Response response = adminUserService.createUser(authorization, create);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody @Valid UserDTO.Update update) {
		logger.info("Request Param [{}, {}]", id, update);
		
		UserDTO.Response response = adminUserService.updateUser(id, update);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<Object> getUser(@PathVariable Long id) {
		logger.info("Request Param [{}]", id);
		
		User user = adminUserService.getUser(id);
		
		List<UserAuthority> userAuthoritys = adminUserAuthorityService.getUserAuthoritys(user);
		
		UserDTO.Response response = modelMapper.map(user, UserDTO.Response.class);
		response.setAuthoritys(userAuthoritys.stream()
											.map(userAuthority -> modelMapper.map(userAuthority.getAuthority(), AuthorityDTO.Response.class))
											.collect(Collectors.toList()));
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/users")
	public ResponseEntity<Object> getUsers(Pageable pageable) {
		logger.info("Request Param [{}]", pageable);
		
		Page<User> users = adminUserService.getUsers(pageable);
		
		return new ResponseEntity<>(users.map(user -> {
			UserDTO.Response response = modelMapper.map(user, UserDTO.Response.class);
			response.setSeqId(user.getId());
			response.add(linkTo(AdminUserResource.class).slash("users").slash(user.getId()).withRel("id"));
			return response;
		}), HttpStatus.OK);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
		logger.info("Request Param [{}]", id);
		
		adminUserService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
