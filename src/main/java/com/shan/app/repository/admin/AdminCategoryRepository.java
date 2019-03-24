package com.shan.app.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shan.app.domain.Category;

public interface AdminCategoryRepository extends JpaRepository<Category, Long>, AdminCategoryRepositoryCustom {

	public Optional<Category> findByCategory(String category);

	public Optional<Category> findByCategoryAndLevel(String category, Integer level);

}
