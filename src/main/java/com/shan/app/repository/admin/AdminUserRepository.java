package com.shan.app.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shan.app.domain.User;

public interface AdminUserRepository extends JpaRepository<User, Long> {

	@EntityGraph(attributePaths="userAuthoritys")
	Optional<User> findWithUserAuthoritiesByUserId(String userId);

	Optional<User> findByUserId(String userId);


}
