package com.shan.app.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shan.app.domain.Code;

public interface AdminCodeRepository extends JpaRepository<Code, Long> {

	Optional<Code> findByCode(String code);

	List<Code> findByParentCode(String parentCode);
	
	List<Code> findByLevel(Integer level);
}
