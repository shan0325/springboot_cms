package com.shan.app.service.admin;

import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shan.app.domain.Authority;
import com.shan.app.error.AuthorityDuplicatedException;
import com.shan.app.error.EntityNotFoundException;
import com.shan.app.repository.admin.AdminAuthorityRepository;
import com.shan.app.service.admin.dto.AuthorityDTO;

@Service
@Transactional
public class AdminAuthorityService {
	
	@Resource(name = "adminAuthorityRepository")
	private AdminAuthorityRepository adminAuthorityRepository;
	

	public Authority createAuthority(@Valid AuthorityDTO.Create create) {
		
		Optional<Authority> optAuthority = adminAuthorityRepository.findById(create.getAuthority());
		if(optAuthority.isPresent()) {
			throw new AuthorityDuplicatedException(create.getAuthority());
		}
		
		Authority authority = new Authority();
		authority.setAuthority(create.getAuthority());
		authority.setAuthorityName(create.getAuthorityName());
		
		return adminAuthorityRepository.save(authority);
	}


	public Authority updateAuthority(String authority, @Valid AuthorityDTO.Update update) {
		Authority updateAuthority = adminAuthorityRepository.findById(authority)
			.map(auth -> {
				auth.setAuthorityName(update.getAuthorityName());
				return auth;
			})
			.orElseThrow(() -> new EntityNotFoundException(Authority.class, "authority", authority));
		
		return adminAuthorityRepository.save(updateAuthority);
	}


	public Page<Authority> getAuthoritys(Pageable pageable) {
		
		return adminAuthorityRepository.findAll(pageable);
	}


	public Authority getAuthority(String authority) {
		
		return adminAuthorityRepository.findById(authority)
										.orElseThrow(() -> new EntityNotFoundException(Authority.class, "authority", authority));
	}

	public void deleteAuthority(String authority) {
		
		adminAuthorityRepository.deleteById(authority);
	}

	
}
