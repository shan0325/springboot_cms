package com.shan.app.service.admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import com.shan.app.domain.Authority;
import com.shan.app.domain.User;
import com.shan.app.domain.UserAuthority;
import com.shan.app.error.EntityNotFoundException;
import com.shan.app.error.PasswordConfirmException;
import com.shan.app.error.UserDuplicatedException;
import com.shan.app.repository.admin.AdminAuthorityRepository;
import com.shan.app.repository.admin.AdminUserAuthorityRepository;
import com.shan.app.repository.admin.AdminUserRepository;
import com.shan.app.security.SecurityUser;
import com.shan.app.service.admin.dto.UserDTO;
import com.shan.app.util.AccessTokenUtil;

@Service
@Transactional
public class AdminUserService {
	
	private final Logger logger = LoggerFactory.getLogger(AdminUserService.class);
	
	@Resource(name="adminUserRepository")
	private AdminUserRepository adminUserRepository;
	
	@Resource(name="adminAuthorityRepository")
	private AdminAuthorityRepository adminAuthorityRepository;
	
	@Resource(name="adminUserAuthorityRepository")
	private AdminUserAuthorityRepository adminUserAuthorityRepository;
	
	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	
	public User createUser(String authorization, UserDTO.Create create) {

		Optional<User> userOptional = adminUserRepository.findByUserId(create.getUserId());
		if(userOptional.isPresent()) {
			throw new UserDuplicatedException(create.getUserId());
		}
		
		User user = new User();
		user.setUserId(create.getUserId());
		user.setName(create.getName());
		user.setEmail(create.getEmail());
		user.setHp(create.getHp());
		user.setTel(create.getTel());
		user.setState(create.getState());
		user.setPassword(bCryptPasswordEncoder.encode(create.getPassword()));
		user.setRegDate(LocalDateTime.now());
		
		OAuth2Authentication authentication = tokenStore.readAuthentication(AccessTokenUtil.deleteBearerFromAuthorization(authorization));
		user.setRegUserId(authentication.getName());
		
		User newUser = adminUserRepository.save(user);
		if(newUser != null) {
			List<String> authoritys = create.getAuthoritys();
			setUserAddUserAuthority(authoritys, newUser);
		}
		
		return newUser;
	}

	public User updateUser(Long id, UserDTO.Update update) {
		
		User updatedUser = adminUserRepository.findById(id)
			.map(u -> {
				u.setName(update.getName());
				u.setEmail(update.getEmail());
				u.setHp(update.getHp());
				u.setTel(update.getTel());
				u.setUpdateDate(LocalDateTime.now());
				
				if(!StringUtils.isBlank(update.getPassword()) || !StringUtils.isBlank(update.getPasswordConfirm())) {
					if(!update.getPassword().equals(update.getPasswordConfirm())) {
						throw new PasswordConfirmException();
					}
					u.setPassword(bCryptPasswordEncoder.encode(update.getPassword()));
				}
				
				return u;
			})
			.orElseThrow(() -> new EntityNotFoundException(User.class, "id", String.valueOf(id)));
		logger.info("Updated User [{}]", updatedUser);
		
		//User updatedUser = adminUserRepository.save(updateUser);
		if(updatedUser != null) {
			List<String> authoritys = update.getAuthoritys();
			//updatedUser.getUserAuthoritys().clear();
			setUserAddUserAuthority(authoritys, updatedUser);
		}
		
		return updatedUser;
	}
	
	public User getUser(Long id) {
		return adminUserRepository.findById(id)
								.orElseThrow(() -> new EntityNotFoundException(User.class, "id", String.valueOf(id)));
	}
	
	public User getUser(String userId) {
		return adminUserRepository.findByUserId(userId)
								.orElseThrow(() -> new EntityNotFoundException(User.class, "userId", userId));
	}
	
	public void setUserAddUserAuthority(List<String> authoritys, User user) {
		if(authoritys != null && authoritys.size() > 0) {
			//삭제 쿼리
			adminUserAuthorityRepository.deleteByUser(user);
			
			for(String auth : authoritys) {
				Optional<Authority> optAuthority = adminAuthorityRepository.findByAuthority(auth);
				Authority authority = optAuthority.orElseThrow(() -> new EntityNotFoundException(Authority.class, "authoritys", auth));
				
				UserAuthority userAuthority = new UserAuthority();
				userAuthority.setUser(user);
				userAuthority.setAuthority(authority);
				
				UserAuthority newUserAuthority = adminUserAuthorityRepository.save(userAuthority);
				//user.addAuthority(newUserAuthority);
			}
		}
	}

	public Page<User> getUsers(Pageable pageable) {
		
		return adminUserRepository.findAll(pageable);
	}

	public void deleteUser(Long id) {
		User user = getUser(id);
		
		// 권한을 먼저 삭제한다.
		adminUserAuthorityRepository.deleteByUser(user);
		
		adminUserRepository.delete(user);
	}

}
