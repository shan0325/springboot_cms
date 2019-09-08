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
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import com.shan.app.controller.admin.AdminManagerResource;
import com.shan.app.domain.Authority;
import com.shan.app.domain.Manager;
import com.shan.app.domain.ManagerAuthority;
import com.shan.app.error.EntityNotFoundException;
import com.shan.app.error.ManagerDuplicatedException;
import com.shan.app.error.PasswordConfirmException;
import com.shan.app.repository.admin.AdminAuthorityRepository;
import com.shan.app.repository.admin.AdminManagerAuthorityRepository;
import com.shan.app.repository.admin.AdminManagerRepository;
import com.shan.app.service.admin.dto.AuthorityDTO;
import com.shan.app.service.admin.dto.ManagerDTO;
import com.shan.app.util.AccessTokenUtil;

@Service
@Transactional
public class AdminManagerService {

	private final Logger logger = LoggerFactory.getLogger(AdminManagerService.class);
	
	@Resource(name="adminManagerRepository")
	private AdminManagerRepository adminManagerRepository;
	
	@Resource(name="adminAuthorityRepository")
	private AdminAuthorityRepository adminAuthorityRepository;
	
	@Resource(name="adminManagerAuthorityRepository")
	private AdminManagerAuthorityRepository adminManagerAuthorityRepository;
	
	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public ManagerDTO.Response createManager(String authorization, ManagerDTO.Create create) {

		Optional<Manager> managerOptional = adminManagerRepository.findByManagerId(create.getManagerId());
		if(managerOptional.isPresent()) {
			throw new ManagerDuplicatedException(create.getManagerId());
		}
		
		Manager manager = new Manager();
		manager.setManagerId(create.getManagerId());
		manager.setName(create.getName());
		manager.setEmail(create.getEmail());
		manager.setHp(create.getHp());
		manager.setTel(create.getTel());
		manager.setState(create.getState());
		manager.setPassword(bCryptPasswordEncoder.encode(create.getPassword()));
		manager.setRegDate(LocalDateTime.now());
		
		OAuth2Authentication authentication = tokenStore.readAuthentication(AccessTokenUtil.deleteBearerFromAuthorization(authorization));
		manager.setRegManagerId(authentication.getName());
		
		Manager newManager = adminManagerRepository.save(manager);
		List<ManagerAuthority> managerAuthoritys = setManagerAddManagerAuthority(create.getAuthoritys(), newManager);
		
		ManagerDTO.Response response = modelMapper.map(newManager, ManagerDTO.Response.class);
		response.setAuthoritys(managerAuthoritys.stream()
											.map(managerAuthority -> modelMapper.map(managerAuthority.getAuthority(), AuthorityDTO.Response.class))
											.collect(Collectors.toList()));
		
		return response;
	}

	public ManagerDTO.Response updateManager(Long id, ManagerDTO.Update update) {
		
		Manager updatedManager = adminManagerRepository.findById(id)
			.map(m -> {
				m.setName(update.getName());
				m.setEmail(update.getEmail());
				m.setHp(update.getHp());
				m.setTel(update.getTel());
				m.setState(update.getState());
				m.setUpdateDate(LocalDateTime.now());
				
				if(!StringUtils.isBlank(update.getPassword()) || !StringUtils.isBlank(update.getPasswordConfirm())) {
					if(!update.getPassword().equals(update.getPasswordConfirm())) {
						throw new PasswordConfirmException();
					}
					m.setPassword(bCryptPasswordEncoder.encode(update.getPassword()));
				}
				
				return m;
			})
			.orElseThrow(() -> new EntityNotFoundException(Manager.class, "id", String.valueOf(id)));
		logger.info("Updated Manager [{}]", updatedManager);
		
		List<ManagerAuthority> managerAuthoritys = setManagerAddManagerAuthority(update.getAuthoritys(), updatedManager);
		
		ManagerDTO.Response response = modelMapper.map(updatedManager, ManagerDTO.Response.class);
		response.setAuthoritys(managerAuthoritys.stream()
											.map(managerAuthority -> modelMapper.map(managerAuthority.getAuthority(), AuthorityDTO.Response.class))
											.collect(Collectors.toList()));
		
		return response;
	}
	
	public Manager getManager(Long id) {
		return adminManagerRepository.findById(id)
								.orElseThrow(() -> new EntityNotFoundException(Manager.class, "id", String.valueOf(id)));
	}
	
	public Manager getManager(String managerId) {
		return adminManagerRepository.findByManagerId(managerId)
								.orElseThrow(() -> new EntityNotFoundException(Manager.class, "managerId", managerId));
	}
	
	public List<ManagerAuthority> setManagerAddManagerAuthority(List<String> authoritys, Manager manager) {
		List<ManagerAuthority> managerAuthoritys = new ArrayList<>();
		
		if(authoritys != null && authoritys.size() > 0) {
			//삭제 쿼리
			adminManagerAuthorityRepository.deleteByManager(manager);
			
			for(String auth : authoritys) {
				Optional<Authority> optAuthority = adminAuthorityRepository.findByAuthority(auth);
				Authority authority = optAuthority.orElseThrow(() -> new EntityNotFoundException(Authority.class, "authoritys", auth));
				
				ManagerAuthority managerAuthority = new ManagerAuthority();
				managerAuthority.setManager(manager);
				managerAuthority.setAuthority(authority);
				
				ManagerAuthority newManagerAuthority = adminManagerAuthorityRepository.save(managerAuthority);
				manager.addAuthority(newManagerAuthority);
				managerAuthoritys.add(newManagerAuthority);
			}
		}
		
		return managerAuthoritys;
	}

	public Page<ManagerDTO.Response> getManagers(Pageable pageable) {
		
		Page<Manager> list = adminManagerRepository.findAll(pageable);
		
		return list.map(manager -> {
			ManagerDTO.Response response = modelMapper.map(manager, ManagerDTO.Response.class);
			response.add(linkTo(AdminManagerResource.class).slash("manager").slash(manager.getId()).withRel("id"));
			return response;
		});
	}

	public void deleteManager(Long id) {
		Manager manager = getManager(id);
		
		// 권한을 먼저 삭제한다.
		adminManagerAuthorityRepository.deleteByManager(manager);
		
		adminManagerRepository.delete(manager);
	}
}
