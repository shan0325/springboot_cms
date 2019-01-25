package com.shan.app.controller.admin;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
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

import com.shan.app.domain.Authority;
import com.shan.app.service.admin.AdminAuthorityService;
import com.shan.app.service.admin.dto.AuthorityDTO;

@RestController
@RequestMapping("/spring-admin/api")
public class AdminAuthorityResource {
	
	private final Logger logger = LoggerFactory.getLogger(AdminAuthorityResource.class);
	
	@Resource(name = "adminAuthorityService")
	private AdminAuthorityService adminAuthorityService;
	
	@Autowired
	private ModelMapper modelMapper;

	
	@PostMapping("/authority")
	public ResponseEntity<Object> createAuthority(@RequestBody @Valid AuthorityDTO.Create create) {
		logger.info("Request Param [{}]", create);
		
		Authority newAuthority = adminAuthorityService.createAuthority(create);
		return new ResponseEntity<>(modelMapper.map(newAuthority, AuthorityDTO.Response.class), HttpStatus.CREATED);
	}
	
	@PutMapping("/authority/{authority}")
	public ResponseEntity<Object> updateAuthority(@PathVariable String authority, @RequestBody @Valid AuthorityDTO.Update update) {
		logger.info("Request Param [{}, {}]", authority, update);
		
		Authority updatedAuthority = adminAuthorityService.updateAuthority(authority, update);
		return new ResponseEntity<>(modelMapper.map(updatedAuthority, AuthorityDTO.Response.class), HttpStatus.OK);
	}
	
	@GetMapping("/authoritys")
	public ResponseEntity<Object> getAuthoritys(Pageable pageable) {
		logger.info("Request Param [{}]", pageable);
		
		Page<Authority> authoritys = adminAuthorityService.getAuthoritys(pageable);
		
		return new ResponseEntity<>(authoritys.map(auth -> {
					AuthorityDTO.Response response = modelMapper.map(auth, AuthorityDTO.Response.class);
					response.add(ControllerLinkBuilder.linkTo(AdminAuthorityResource.class).slash("authority").slash(auth.getAuthority()).withRel("authority"));
					return response;
				}), HttpStatus.OK);
	}
	
	@GetMapping("/authority/{authority}")
	public ResponseEntity<Object> getAuthority(@PathVariable String authority) {
		logger.info("Request Param [{}]", authority);
		
		Authority auth = adminAuthorityService.getAuthority(authority);
		return new ResponseEntity<>(modelMapper.map(auth, AuthorityDTO.Response.class), HttpStatus.OK);
	}
	
	@DeleteMapping("/authority/{authority}")
	public ResponseEntity<Object> deleteAuthority(@PathVariable String authority) {
		logger.info("Request Param [{}]", authority);
		
		adminAuthorityService.deleteAuthority(authority);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
