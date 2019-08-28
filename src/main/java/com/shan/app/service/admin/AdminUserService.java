package com.shan.app.service.admin;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import com.querydsl.core.Tuple;
import com.shan.app.controller.admin.AdminUserResource;
import com.shan.app.domain.Authority;
import com.shan.app.domain.User;
import com.shan.app.domain.UserAuthority;
import com.shan.app.error.EntityNotFoundException;
import com.shan.app.error.PasswordConfirmException;
import com.shan.app.error.UserDuplicatedException;
import com.shan.app.repository.admin.AdminAuthorityRepository;
import com.shan.app.repository.admin.AdminUserAuthorityRepository;
import com.shan.app.repository.admin.AdminUserRepository;
import com.shan.app.service.admin.dto.AuthorityDTO;
import com.shan.app.service.admin.dto.UserDTO;
import com.shan.app.service.admin.dto.UserDTO.Response;
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
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public UserDTO.Response createUser(String authorization, UserDTO.Create create) {

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
		List<UserAuthority> userAuthoritys = setUserAddUserAuthority(create.getAuthoritys(), newUser);
		
		UserDTO.Response response = modelMapper.map(newUser, UserDTO.Response.class);
		response.setAuthoritys(userAuthoritys.stream()
											.map(userAuthority -> modelMapper.map(userAuthority.getAuthority(), AuthorityDTO.Response.class))
											.collect(Collectors.toList()));
		
		return response;
	}

	public UserDTO.Response updateUser(Long id, UserDTO.Update update) {
		
		User updatedUser = adminUserRepository.findById(id)
			.map(u -> {
				u.setName(update.getName());
				u.setEmail(update.getEmail());
				u.setHp(update.getHp());
				u.setTel(update.getTel());
				u.setState(update.getState());
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
		
		List<UserAuthority> userAuthoritys = setUserAddUserAuthority(update.getAuthoritys(), updatedUser);
		
		UserDTO.Response response = modelMapper.map(updatedUser, UserDTO.Response.class);
		response.setAuthoritys(userAuthoritys.stream()
											.map(userAuthority -> modelMapper.map(userAuthority.getAuthority(), AuthorityDTO.Response.class))
											.collect(Collectors.toList()));
		
		return response;
	}
	
	public User getUser(Long id) {
		return adminUserRepository.findById(id)
								.orElseThrow(() -> new EntityNotFoundException(User.class, "id", String.valueOf(id)));
	}
	
	public User getUser(String userId) {
		return adminUserRepository.findByUserId(userId)
								.orElseThrow(() -> new EntityNotFoundException(User.class, "userId", userId));
	}
	
	public List<UserAuthority> setUserAddUserAuthority(List<String> authoritys, User user) {
		List<UserAuthority> userAuthoritys = new ArrayList<>();
		
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
				user.addAuthority(newUserAuthority);
				userAuthoritys.add(newUserAuthority);
			}
		}
		
		return userAuthoritys;
	}

	public Page<UserDTO.Response> getUsers(Pageable pageable) {
		
		PageImpl<User> list = adminUserRepository.findList(pageable);
		
		return list.map(user -> {
			UserDTO.Response response = modelMapper.map(user, UserDTO.Response.class);
			response.add(linkTo(AdminUserResource.class).slash("users").slash(user.getId()).withRel("id"));
			return response;
		});
	}

	public void deleteUser(Long id) {
		User user = getUser(id);
		
		// 권한을 먼저 삭제한다.
		adminUserAuthorityRepository.deleteByUser(user);
		
		adminUserRepository.delete(user);
	}

}
