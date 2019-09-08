package com.shan.app.controller.admin;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shan.app.domain.Manager;
import com.shan.app.domain.ManagerAuthority;
import com.shan.app.service.admin.AdminManagerAuthorityService;
import com.shan.app.service.admin.AdminManagerService;
import com.shan.app.service.admin.dto.AuthorityDTO;
import com.shan.app.service.admin.dto.ManagerDTO;

@RestController
@RequestMapping("/spring-admin/api")
public class AdminManagerResource {
	
	private final Logger logger = LoggerFactory.getLogger(AdminManagerResource.class);
			
	@Resource(name="adminManagerService")
	private AdminManagerService adminManagerService;
	
	@Resource(name="adminManagerAuthorityService")
	private AdminManagerAuthorityService adminManagerAuthorityService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping("/managers")
	public ResponseEntity<Object> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody @Valid ManagerDTO.Create create) {
		logger.info("Request Param [{}, {}]", authorization, create);
		
		ManagerDTO.Response response = adminManagerService.createManager(authorization, create);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/managers/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid ManagerDTO.Update update) {
		logger.info("Request Param [{}, {}]", id, update);
		
		ManagerDTO.Response response = adminManagerService.updateManager(id, update);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/managers/{id}")
	public ResponseEntity<Object> one(@PathVariable Long id) {
		logger.info("Request Param [{}]", id);
		
		Manager manager = adminManagerService.getManager(id);
		
		List<ManagerAuthority> managerAuthoritys = adminManagerAuthorityService.getManagerAuthoritys(manager);
		
		ManagerDTO.Response response = modelMapper.map(manager, ManagerDTO.Response.class);
		response.setAuthoritys(managerAuthoritys.stream()
											.map(managerAuthority -> modelMapper.map(managerAuthority.getAuthority(), AuthorityDTO.Response.class))
											.collect(Collectors.toList()));
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/managers")
	public ResponseEntity<Object> all(Pageable pageable) {
		logger.info("Request Param [{}]", pageable);
		
		Page<ManagerDTO.Response> responses = adminManagerService.getManagers(pageable);
		
		return new ResponseEntity<>(responses, HttpStatus.OK);
	}
	
	@DeleteMapping("/managers/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		logger.info("Request Param [{}]", id);
		
		adminManagerService.deleteManager(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
			

}
