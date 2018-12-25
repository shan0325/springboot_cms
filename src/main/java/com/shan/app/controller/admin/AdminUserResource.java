package com.shan.app.controller.admin;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shan.app.domain.User;
import com.shan.app.service.admin.AdminUserService;
import com.shan.app.service.admin.dto.UserDTO;

@RestController
@RequestMapping("/spring-admin/api")
public class AdminUserResource {
	
	private final Logger logger = LoggerFactory.getLogger(AdminUserResource.class);

	@Resource(name="adminUserService")
	private AdminUserService adminUserService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping("/user")
	public ResponseEntity<Object> createUser(@RequestBody @Valid UserDTO.Create create) {
		logger.info("Request Param [{}]", create);
		
		User newUser = adminUserService.createUser(create);
		return new ResponseEntity<>(modelMapper.map(newUser, UserDTO.Response.class), HttpStatus.CREATED);
	}
	
	@PutMapping("/user/{userId}")
	public ResponseEntity<Object> updateUser(@PathVariable String userId, @RequestBody @Valid UserDTO.Update update) {
		logger.info("Request Param [{}, {}]", userId, update);
		
		User updatedUser = adminUserService.updateUser(userId, update);
		return new ResponseEntity<>(modelMapper.map(updatedUser, UserDTO.Response.class), HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<Object> getUser(@PathVariable String userId) {
		logger.info("Request Param [{}]", userId);
		
		User user = adminUserService.getUser(userId);
		return new ResponseEntity<>(modelMapper.map(user, UserDTO.Response.class), HttpStatus.OK);
	}
	
	@GetMapping("/users")
	public ResponseEntity<Object> getUsers(Pageable pageable) {
		logger.info("Request Param [{}]", pageable);
		
		Page<User> users = adminUserService.getUsers(pageable);
		
		return new ResponseEntity<>(users.map(user -> {
					UserDTO.Response response = modelMapper.map(user, UserDTO.Response.class);
					response.add(linkTo(AdminUserResource.class).slash("user").slash(user.getUserId()).withRel("user"));
					return response;
				}), HttpStatus.OK);
	}
	
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<Object> deleteUser(@PathVariable String userId) {
		logger.info("Request Param [{}]", userId);
		
		adminUserService.deleteUser(userId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
