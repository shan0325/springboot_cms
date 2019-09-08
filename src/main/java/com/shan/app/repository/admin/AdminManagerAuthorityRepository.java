package com.shan.app.repository.admin;

import java.util.List;

import javax.jdo.annotations.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shan.app.domain.Manager;
import com.shan.app.domain.ManagerAuthority;
import com.shan.app.domain.ManagerAuthorityId;

public interface AdminManagerAuthorityRepository extends JpaRepository<ManagerAuthority, ManagerAuthorityId> {

	@Transactional
	@Modifying
	@Query("DELETE FROM ManagerAuthority m WHERE m.manager = :manager")
	int deleteByManager(@Param("manager") Manager manager);
	
	@Transactional
	@Modifying
	@Query("SELECT m FROM ManagerAuthority m WHERE m.manager = :manager")
	List<ManagerAuthority> findByManager(@Param("manager") Manager manager);
}
