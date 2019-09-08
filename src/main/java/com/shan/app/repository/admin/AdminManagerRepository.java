package com.shan.app.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shan.app.domain.Manager;

public interface AdminManagerRepository extends JpaRepository<Manager, Long> {

	@EntityGraph(attributePaths="managerAuthoritys")
	Optional<Manager> findWithManagerAuthoritiesByManagerId(String managerId);

	Optional<Manager> findByManagerId(String managerId);
}
