package com.shan.app.controller.admin;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@PutMapping("/code/{id}")
	public ResponseEntity<Object> updateCode(@PathVariable Long id, @RequestBody @Valid CodeDTO.Update update) {
		logger.info("Request Param [{}, {}]", id, update);
		
		Code updatedCode = adminCodeService.updateCode(id, update);
		return new ResponseEntity<>(modelMapper.map(updatedCode, CodeDTO.Response.class), HttpStatus.OK);
	}
	
	@GetMapping("/codes")
	public ResponseEntity<Object> getCodes() {
		
		List<Code> codes = adminCodeService.getCodes();
		return new ResponseEntity<>(codes.stream()
											.map(code -> {
												return modelMapper.map(code, CodeDTO.Response.class);
											})
											.collect(Collectors.toList()), HttpStatus.OK);
	}
	
	@GetMapping("/code/{id}")
	public ResponseEntity<Object> getCode(@PathVariable Long id) {
		logger.info("Request Param [{}]", id);
		
		Code code = adminCodeService.getCode(id);
		return new ResponseEntity<>(modelMapper.map(code, CodeDTO.Response.class), HttpStatus.OK);
	}
	
	@DeleteMapping("/code/{id}")
	public ResponseEntity<Object> deleteCode(@PathVariable Long id) {
		logger.info("Request Param [{}]", id);

		adminCodeService.deleteCode(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
