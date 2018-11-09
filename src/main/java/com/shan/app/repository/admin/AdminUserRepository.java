package com.shan.app.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shan.app.domain.User;

public interface AdminUserRepository extends JpaRepository<User, String> {

	Optional<User> findOneWithUserAuthoritiesByUserId(String userId);


}
