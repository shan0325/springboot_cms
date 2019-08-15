package com.shan.app.repository.admin;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shan.app.domain.Authority;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminAuthorityRepositoryTest {

	@Autowired
	private AdminAuthorityRepository adminAuthorityRepository;
	
	
	@Test
	public void findOneByAuthorityTest() {
		Optional<Authority> authority = adminAuthorityRepository.findById(2L);
		
		assertThat(authority, is(notNullValue()));
		assertThat(authority.get().getAuthority(), is("MEMBER"));
	}
}
