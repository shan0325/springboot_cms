package com.shan.app.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shan.app.domain.User;

public interface AdminUserRepository extends JpaRepository<User, String> {

	@EntityGraph(attributePaths="userAuthoritys")
	Optional<User> findOneWithUserAuthoritiesByUserId(String userId);

	User findOneByUserId(String userId);


}
