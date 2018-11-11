package com.shan.app.controller.admin;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shan.app.domain.User;
import com.shan.app.service.admin.AdminUserService;
import com.shan.app.service.admin.dto.UserDTO;

@RestController
@RequestMapping("/spring")
public class AdminUserResource {
	
	private final Logger logger = LoggerFactory.getLogger(AdminUserResource.class);

	@Resource(name="adminUserService")
	private AdminUserService adminUserService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping("/user")
	public ResponseEntity<Object> createUser(@RequestBody @Valid UserDTO.Create userDTO) {
		
		User newUser = adminUserService.createUser(userDTO);
		logger.debug("newUser = " + newUser);
		
		return new ResponseEntity<>(modelMapper.map(newUser, UserDTO.Response.class), HttpStatus.CREATED);
	}
	
}
