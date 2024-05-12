package com.skybory.seoulArt.Oauth;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.skybory.seoulArt.domain.user.dto.UserDTO;
import com.skybory.seoulArt.domain.user.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
 
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtil jwtUtil;

	public JwtAuthenticationFilter(UserService userService, JwtUtil jwtUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	        throws ServletException, IOException {
		log.info("필터 작동 : JWT 필요"); 
	    final String authorizationHeader = request.getHeader("Authorization");
	    log.info("헤더에서 JWT 가져오기");
	    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	        String jwt = authorizationHeader.substring(7);
	        if (jwtUtil.validateToken(jwt)) {
	            try {
	            	log.info("validateToken 값이 true 입니다");
	                Long userId = jwtUtil.getUserIdFromToken(jwt); // JWT에서 사용자 이름 추출
	                UserDTO user = userService.loadUserByUserId(userId); // UserService를 통해 UserDTO 조회

	                if (user instanceof UserDTO) { // 타입 검증
	                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
	                        user, null, Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
	                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	                } else {
	                    log.error("Expected user object to be an instance of UserDTO but got: " + user.getClass());
	                    SecurityContextHolder.clearContext();
	                }
	            } catch (Exception e) {
	                log.error("Authentication error: " + e.getMessage());
	                SecurityContextHolder.clearContext();
	            }
	        }
	        log.info("validateToken 값이 false 입니다");
	    }
	    else {
	    	log.info("JWT 가져오기 실패");
	    }
	    chain.doFilter(request, response);
	}
	
	
}
