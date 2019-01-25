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

import com.shan.app.domain.Code;
import com.shan.app.service.admin.AdminCodeService;
import com.shan.app.service.admin.dto.CodeDTO;

@RestController
@RequestMapping("/spring-admin/api")
public class AdminCodeResource {

	private final Logger logger = LoggerFactory.getLogger(AdminCodeResource.class);
	
	@Resource(name="adminCodeService")
	private AdminCodeService adminCodeService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping("/code")
	public ResponseEntity<Object> createCode(@RequestBody @Valid CodeDTO.Create create) {
		logger.info("Request Param [{}]", create);
		
		Code newCode = adminCodeService.createCode(create);
		return new ResponseEntity<>(modelMapper.map(newCode, CodeDTO.Response.class), HttpStatus.CREATED);
	}
}
