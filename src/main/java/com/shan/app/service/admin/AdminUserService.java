package com.shan.app.service.admin;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shan.app.domain.Authority;
import com.shan.app.domain.User;
import com.shan.app.domain.UserAuthority;
import com.shan.app.error.EntityDuplicatedException;
import com.shan.app.error.EntityNotFoundException;
import com.shan.app.repository.admin.AdminAuthorityRepository;
import com.shan.app.repository.admin.AdminUserRepository;
import com.shan.app.security.SecurityUser;
import com.shan.app.service.admin.dto.UserDTO;

@Service
public class AdminUserService {
	
	@Resource(name="adminUserRepository")
	private AdminUserRepository adminUserRepository;
	
	@Resource(name="adminAuthorityRepository")
	private AdminAuthorityRepository adminAuthorityRepository;
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	

	public User createUser(UserDTO.Create userDTO) {
		
		User userDetail = adminUserRepository.findOneByUserId(userDTO.getUserId());
		if(userDetail != null) {
			throw new EntityDuplicatedException(User.class, "userId", userDTO.getUserId());
		}
		
		User user = new User();
		user.setUserId(userDTO.getUserId());
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());
		user.setHp(userDTO.getHp());
		user.setTel(userDTO.getTel());
		user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
		user.setRegDate(new Date());
		
		List<String> authorities = userDTO.getAuthorities();
		authorities.forEach(auth -> {
			Optional<Authority> optAuthority = adminAuthorityRepository.findById(auth);
			Authority authority = optAuthority.map(d -> d)
										.orElseThrow(() -> new EntityNotFoundException(Authority.class, "id", auth));
			
			UserAuthority userAuthority = new UserAuthority();
			userAuthority.setUser(user);
			userAuthority.setAuthority(authority);
			
			user.addAuthority(userAuthority);
		});
		
		//시큐리티 세션 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
		user.setRegUserId(securityUser.getUsername());
		
		return adminUserRepository.save(user);
	}

}
