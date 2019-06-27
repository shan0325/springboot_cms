package com.shan.app.controller.admin;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shan.app.domain.User;
import com.shan.app.service.admin.AdminUserService;
import com.shan.app.service.admin.dto.AuthorityDTO;
import com.shan.app.service.admin.dto.LoginDTO;

@RestController
@RequestMapping("/spring-admin")
public class AdminLoginResource {
	
	private final Logger logger = LoggerFactory.getLogger(AdminLoginResource.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Resource(name="adminUserService")
	private AdminUserService adminUserService;
	
	@Autowired
	private ModelMapper modelMapper;
	

	/**
	 *   
	 . AuthenticationManager는 설정에서 Bean으로 등록했기 때문에 @Autowired를 통해 Injection 해줍니다.
     . URL 경로는 /login으로 하고, method는 POST 방식으로 합니다. Body에 데이터를 넣어서 호출할 때는 POST 방식을 사용합니다. 왜냐구요? login을 url을 통해 노출시킬 순 없으니깐요.
     . @RequestBody를 통해 Body의 데이터를 해당 객체로 받습니다. JSON 형태로 받는 경우 객체로 자동 매핑됩니다.
     . 처음에 username과 passwor를 통해 UsernamePasswordAuthenticationToken을 만듭니다. (반드시 이거일 필요는 없지만 Authentication 인터페이스를 구현한 것이어야 합니다.)
     . 다음은 AuthenticationManager의 authenticate 메소드에 위에서 만든 token을 파라미터로 하여 인증을 진행합니다. 이 때 SpringSecurity 설정한 인증이 적용됩니다.
     . 인증받은 결과를 SecurityContextHolder에서 getContext()를 통해 context를 받아온 후, 이 context에 인증 결과를 set 해줍니다. 이로써 서버의 SecurityContext에는 인증값이 설정됩니다.
     . Controller에서 HttpSession session을 파라미터로 지정하면 session을 받아올 수 있습니다. session 속성값에 SecurityContext 값을 넣어줍니다. 
     . 이 때 속성키는 HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY로 넣어주면 됩니다.
     . 인증을 완료한 후 session에도 셋팅을 완료했으면 user를 조회해서 사용자 계정, 권한, session id로 AuthenticationToken 객체를 만들어 리턴해줍니다.
	 * 
	 * @param login
	 * @param session
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody @Valid LoginDTO.Login login, HttpSession session) {
		logger.info("Request Param [{}]", login);
		
		String userId = login.getUserId();
		String password = login.getPassword();
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userId, password);
		Authentication authentication = authenticationManager.authenticate(token);
		logger.info("authentication : " + authentication);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
		
		User user = adminUserService.getUser(userId);
		
		List<AuthorityDTO.Response> authoritys = new ArrayList<AuthorityDTO.Response>();
		user.getUserAuthoritys().forEach(userAuthority -> {
			authoritys.add(modelMapper.map(userAuthority.getAuthority(), AuthorityDTO.Response.class));
		});
		
		logger.info("login sessionId : " + session.getId());
		LoginDTO.LoginToken loginToken = new LoginDTO.LoginToken(user.getUserId(), authoritys, session.getId());
		
		return new ResponseEntity<>(loginToken, HttpStatus.OK);
	}
	
	/**
	 * 로그아웃 처리
	 * @param session
	 * @return
	 */
	@GetMapping("/logout")
	public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		logger.info("authentication : " + authentication);
		
		Enumeration<String> attributeNames = request.getSession().getAttributeNames();
		while(attributeNames.hasMoreElements()) {
			System.out.println("session attr : " + attributeNames.nextElement());
		}
		
		if(authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
