package com.shan.app.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shan.app.domain.Authority;

public interface AdminAuthorityRepository extends JpaRepository<Authority, String> {

}
