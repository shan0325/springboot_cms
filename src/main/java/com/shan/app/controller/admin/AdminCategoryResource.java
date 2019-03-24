package com.shan.app.controller.admin;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shan.app.domain.Category;
import com.shan.app.service.admin.AdminCategoryService;
import com.shan.app.service.admin.dto.CategoryDTO;

@RestController
@RequestMapping("/spring-admin/api")
public class AdminCategoryResource {
	
	private final Logger logger = LoggerFactory.getLogger(AdminCategoryResource.class);
	
	@Resource(name = "adminCategoryService")
	private AdminCategoryService adminCategoryService;
	
	@Autowired
	private ModelMapper modelMapper;

	
	@PostMapping("/category")
	public ResponseEntity<Object> createCategory(@RequestBody @Valid CategoryDTO.Create create) {
		logger.info("Request Param [{}]", create);
		
		Category newCategory = adminCategoryService.createCategory(create);
		return new ResponseEntity<>(modelMapper.map(newCategory, CategoryDTO.Response.class), HttpStatus.CREATED);
	}
	
	@PutMapping("/category/{id}")
	public ResponseEntity<Object> updteCategory(@PathVariable Long id, @RequestBody @Valid CategoryDTO.Update update) {
		logger.info("Request Param [{}, {}]", id, update);
				
		Category updatedCategory = adminCategoryService.updateCategory(id, update);
		return new ResponseEntity<>(modelMapper.map(updatedCategory, CategoryDTO.Response.class), HttpStatus.OK);
	}
	
	@GetMapping("/categorys")
	public ResponseEntity<Object> getCategorys() {
		
		List<Category> categorys = adminCategoryService.getCategorys();
		return new ResponseEntity<>(categorys.stream()
												.map(category -> {
													return modelMapper.map(category, CategoryDTO.Response.class);
												})
												.collect(Collectors.toList()), HttpStatus.OK);
	}
	
	@GetMapping("/category/{id}")
	public ResponseEntity<Object> getCategory(@PathVariable Long id) {
		logger.info("Request Param [{}]", id);
		
		Category category = adminCategoryService.getCategory(id);
		return new ResponseEntity<>(modelMapper.map(category, CategoryDTO.Response.class), HttpStatus.OK);
	}
}
