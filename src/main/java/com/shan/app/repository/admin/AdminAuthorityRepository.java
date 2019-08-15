package com.shan.app.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shan.app.domain.Authority;

public interface AdminAuthorityRepository extends JpaRepository<Authority, Long> {
	
	Optional<Authority> findByAuthority(String authority);

}
