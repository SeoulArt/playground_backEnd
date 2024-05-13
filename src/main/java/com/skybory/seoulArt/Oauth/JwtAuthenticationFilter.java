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
//		final String authorizationHeader = request.getHeader("Authorization");
		final String accessToken = extractTokenFromCookie(request, "accessToken");
		final String refreshToken = extractTokenFromCookie(request, "refreshToken");
		if (accessToken != null && refreshToken != null) {
//			String jwt = authorizationHeader.substring(7);
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
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No JWT token found");
			}
		} else {
			log.info("JWT 가져오기 실패");
		}
		chain.doFilter(request, response);
	}
 
	private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
	    log.info("쿠키에서 {} 가져오기", cookieName);
	    if (request.getCookies() != null) {
	        for (Cookie cookie : request.getCookies()) {
	            if (cookieName.equals(cookie.getName())) {
	                return cookie.getValue();
	            }
	        }
	    }
	    return null;
	}
}
//    private void authenticateUser(HttpServletRequest request, String jwt) {
//        try {
//            Long userId = jwtUtil.getUserIdFromToken(jwt);  // Extract user ID from JWT
//            UserDTO user = userService.loadUserByUserId(userId);  // Load user details
//            if (user != null) {
//            	UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//						user, null, Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
//      			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                log.info("Set security context for user ID {}", userId);
//            } else {
//                log.error("User not found with ID: {}", userId);
//            }
//        } catch (Exception e) {
//            log.error("Authentication error: {}", e.getMessage());
//            SecurityContextHolder.clearContext();
//        }
//        
//    }

//	                } else {
//	                    log.error("User object not found with ID: " + userId);
//	                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
//	                    return;
//	                }
//	            } catch (Exception e) {
//	                log.error("Authentication error: " + e.getMessage());
//	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
//	                return;
//	            }
//	        } else {
//	            log.info("validateToken 값이 false 입니다");
//	            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
//	            return;
//	        }
//	    } else {
//	        log.info("JWT 가져오기 실패");
//	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No JWT token found");
//	        return;
//	    }
//	    chain.doFilter(request, response);
//	}
//}
