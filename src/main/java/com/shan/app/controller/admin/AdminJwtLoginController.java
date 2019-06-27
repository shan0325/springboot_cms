package com.shan.app.controller.admin;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shan.app.domain.User;
import com.shan.app.service.admin.AdminUserService;
import com.shan.app.service.admin.dto.AuthorityDTO;
import com.shan.app.service.admin.dto.LoginDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/spring-admin")
public class AdminJwtLoginController {
	
	private final Logger logger = LoggerFactory.getLogger(AdminJwtLoginController.class);
	
	//2hour
    static final long EXPIRATIONTIME = 7200000; 
    static final String SECRET = "spring";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";
    
    @Autowired
	private AuthenticationManager authenticationManager;
    
    @Resource(name="adminUserService")
	private AdminUserService adminUserService;
    
    @Autowired
	private ModelMapper modelMapper;

	@PostMapping("/auth/login")
	public ResponseEntity<Object> authLogin(@Valid LoginDTO.Login login, HttpSession session) {
		
		String userId = login.getUserId();
		String password = login.getPassword();
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userId, password);
		Authentication authentication = authenticationManager.authenticate(token);
		logger.info("authentication : " + authentication);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
		
		User user = adminUserService.getUser(userId);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");
		headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
		headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Authorization");
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization");
		
		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		String jwt = Jwts.builder()
						.setSubject(user.getUserId())
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
						.claim("name", user.getName())
						.claim("scope", "self groups/admins")
						.signWith(key)
						.compact();
		
		headers.add(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + " " + jwt);
		
		List<AuthorityDTO.Response> authoritys = new ArrayList<AuthorityDTO.Response>();
		user.getUserAuthoritys().forEach(userAuthority -> {
			authoritys.add(modelMapper.map(userAuthority.getAuthority(), AuthorityDTO.Response.class));
		});
		
		LoginDTO.LoginToken loginToken = new LoginDTO.LoginToken(userId, authoritys, jwt);
		logger.info("loginToken : " + loginToken);
		
		return new ResponseEntity<>(loginToken, HttpStatus.OK);
	}
	
	@PostMapping("/auth/refresh2")
	public ResponseEntity<Object> authRefresh(HttpServletRequest request, HttpServletResponse response) {
		
		String jwtstr = request.getHeader("Authorization");
        jwtstr = jwtstr.replace("Bearer ", "");
        
        try {
        	Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        	
	        //코드를 체크 해주는 부분 입니다. 
	        Jws<Claims> claims = Jwts.parser()
					                  .setSigningKey(key)
					                  .parseClaimsJws(jwtstr);
	        String userId = (String) claims.getBody().getSubject();
	        String scope = (String) claims.getBody().get("scope");
					
			User user = adminUserService.getUser(userId);
		
			String jwt = Jwts.builder()
							.setSubject(user.getUserId())
							.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
							.claim("name", user.getName())
							.claim("scope", scope)
							.signWith(key)
							.compact();
			
			List<AuthorityDTO.Response> authoritys = new ArrayList<AuthorityDTO.Response>();
			user.getUserAuthoritys().forEach(userAuthority -> {
				authoritys.add(modelMapper.map(userAuthority.getAuthority(), AuthorityDTO.Response.class));
			});
			
			LoginDTO.LoginToken loginToken = new LoginDTO.LoginToken(userId, authoritys, jwt);
			logger.info("loginToken : " + loginToken);
		
			return new ResponseEntity<>(loginToken, HttpStatus.OK);
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
	}
}
