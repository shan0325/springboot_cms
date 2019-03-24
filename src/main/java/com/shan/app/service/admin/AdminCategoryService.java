package com.shan.app.service.admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.shan.app.domain.Category;
import com.shan.app.error.CategoryDuplicatedException;
import com.shan.app.error.EntityNotFoundException;
import com.shan.app.repository.admin.AdminCategoryRepository;
import com.shan.app.service.admin.dto.CategoryDTO;

@Service
@Transactional
public class AdminCategoryService {
	
	@Resource(name = "adminCategoryRepository")
	private AdminCategoryRepository adminCategoryRepository;

	public Category createCategory(CategoryDTO.Create create) {
		
		// 부모 카테고리가 있을경우
		if(create.getParentId() != null) {
			Optional<Category> parentCategoryOptional = adminCategoryRepository.findById(create.getParentId());
			Category parentCategory = parentCategoryOptional.orElseThrow(() -> new EntityNotFoundException(Category.class, "parentId", create.getParentId().toString()));

			Category category = new Category();
			category.setCategory(create.getCategory());
			category.setCategoryName(create.getCategoryName());
			category.setCategoryDesc(create.getCategoryDesc());
			category.setParentCategory(parentCategory.getCategory());
			category.setTopCategory(parentCategory.getTopCategory());
			category.setLevel(parentCategory.getLevel() + 1);
			
			if(create.getOrd() != null) {
				category.setOrd(create.getOrd());
			} else {
				Integer maxOrd = adminCategoryRepository.getMaxOrdByLevel(parentCategory.getLevel() + 1);
				category.setOrd(maxOrd + 1);
			}
			
			category.setUseYn("Y");
			category.setRegDate(LocalDateTime.now());
			
			return adminCategoryRepository.save(category);
			
		} else {
			Optional<Category> categoryOptional = adminCategoryRepository.findByCategoryAndLevel(create.getCategory(), 1);
			if(categoryOptional.isPresent()) {
				throw new CategoryDuplicatedException(create.getCategory());
			}
			
			Category category = new Category();
			category.setCategory(create.getCategory());
			category.setCategoryName(create.getCategoryName());
			category.setCategoryDesc(category.getCategoryDesc());
			category.setTopCategory(create.getCategory());
			category.setLevel(1);
			
			if(create.getOrd() != null) {
				category.setOrd(create.getOrd());
			} else {
				Integer maxOrd = adminCategoryRepository.getMaxOrdByLevel(1);
				category.setOrd(maxOrd + 1);
			}
			
			category.setUseYn("Y");
			category.setRegDate(LocalDateTime.now());
			
			return adminCategoryRepository.save(category);
		}
	}

	public Category updateCategory(Long id, @Valid CategoryDTO.Update update) {
		
		Optional<Category> categoryOptional = adminCategoryRepository.findById(id);
		Category category = categoryOptional.orElseThrow(() -> new EntityNotFoundException(Category.class, "id", id.toString()));
		
		category.setCategoryName(update.getCategoryName());
		category.setCategoryDesc(update.getCategoryDesc());
		category.setUseYn(update.getUseYn());
		category.setOrd(update.getOrd());
		category.setUpdateDate(LocalDateTime.now());
		
		return adminCategoryRepository.save(category);
	}

	public List<Category> getCategorys() {
		
		return adminCategoryRepository.findAll();
	}

	public Category getCategory(Long id) {
		
		return adminCategoryRepository.findById(id)
										.orElseThrow(() -> new EntityNotFoundException(Category.class, "id", id.toString()));
	}

	
}
