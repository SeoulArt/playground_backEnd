package com.skybory.seoulArt.Oauth;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.skybory.seoulArt.domain.user.dto.UserDTO;
import com.skybory.seoulArt.domain.user.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
		final String header = request.getHeader("Authorization");
//		final String accessToken = extractTokenFromCookie(request, "accessToken");
//		final String refreshToken = extractTokenFromCookie(request, "refreshToken");
//		if (accessToken != null && refreshToken != null) {
		if (header != null ) {
			String accessToken = header.substring(7);
			log.info("엑세스 토큰, 리프레시 토큰 추출 성공");
			
			if (jwtUtil.validateToken(accessToken)) {
				try {
					log.info("accessToken 이 유효합니다.");
					Long userId = jwtUtil.getUserIdFromToken(accessToken); // JWT에서 사용자 이름 추출
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
			} else {
				log.info("validateToken 값이 false 입니다");
//				throw new AccessDeniedException("ValidateToken 값이 유효하지 않습니다");
//				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No JWT token found");
//				return;
			}
		} else {
			log.info("JWT AccessToken or JWT RefreshToken 이 null입니다");
		}
		chain.doFilter(request, response);
	}
//	@Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
////	    String requestPath = request.getRequestURI();
////
////	    // JWT 인증이 필요한 경로 정의
////	    List<String> protectedPaths = Arrays.asList("/api/ticket/**");
////
////	    // 현재 경로가 보호된 경로 중 하나인지 확인
////	    boolean isProtectedPath = protectedPaths.stream().anyMatch(path -> pathMatcher.match(path, requestPath));
//
//        log.info("필터 작동 : JWT 필요");
//        final String authorizationHeader = request.getHeader("Authorization");
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String accessToken = authorizationHeader.substring(7);
//            log.info("Authorization 헤더에서 accessToken 추출 성공");
//
//            if (jwtUtil.validateToken(accessToken)) {
//                try {
//                    log.info("accessToken 이 유효합니다.");
//                    Long userId = jwtUtil.getUserIdFromToken(accessToken);
//                    UserDTO user = userService.loadUserByUserId(userId);
//
//                    if (user != null) {
//                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                                user, null, Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
//                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                        log.info("Set security context for user ID {}", userId);
//                    } else {
//                        log.error("User not found with ID: {}", userId);
//                        SecurityContextHolder.clearContext();
//                    }
//                } catch (Exception e) {
//                    log.error("Authentication error: " + e.getMessage());
//                    SecurityContextHolder.clearContext();
//                }
//            } else {
//                log.info("accessToken 유효하지 않음");
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
//                return;
//            }
//        } else {
//            log.info("Authorization 헤더가 Bearer 토큰을 포함하지 않음");
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No JWT token found");
////            return;
//        }
//        chain.doFilter(request, response);
//    }
//	private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
//	    log.info("쿠키에서 {} 가져오기", cookieName);
//	    if (request.getCookies() != null) {
//	        for (Cookie cookie : request.getCookies()) {
//	            if (cookieName.equals(cookie.getName())) {
//	                return cookie.getValue();
//	            }
//	        }
//	    }
//	    return null;
//	}
}



 