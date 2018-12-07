package com.shan.app.repository.admin;

import javax.jdo.annotations.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shan.app.domain.User;
import com.shan.app.domain.UserAuthority;
import com.shan.app.domain.UserAuthorityId;

public interface AdminUserAuthorityRepository extends JpaRepository<UserAuthority, UserAuthorityId> {

	@Transactional
	@Modifying
	@Query("DELETE FROM UserAuthority u WHERE u.user = :user")
	int deleteByUser(@Param("user") User user);

}
