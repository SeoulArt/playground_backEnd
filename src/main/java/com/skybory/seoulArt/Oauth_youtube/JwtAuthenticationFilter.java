//package com.skybory.seoulArt.Oauth_youtube;
//
//import java.io.IOException;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.skybory.seoulArt.domain.user.UserRepository;
//import com.skybory.seoulArt.domain.user.entity.User;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//	private final UserRepository userRepository;
//	private final JwtProvider jwtProvider;
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		
//		try {
//		
//			String token = parseBearerToken(request);
//			if (token == null) {
//				filterChain.doFilter(request, response);
//				return;
//			}
//			
//			// validate 실패하면 null 반환함.
//			String userId = jwtProvider.validate(token);
//			if(userId == null) {
//				filterChain.doFilter(request, response);
//				return;
//			}
//			
//			// 여기서 문제가, userId가 스트링값이 아니라는것임.
//			// 스트링값인 유저아이디라면, 유저정보를 꺼내올 수 있을텐데...
//			User user = userRepository.findByUserId(userId);
//			
//			// 유저의 역할 꺼내오기. 아직 지정하지 않음.
//			String role = user.getRole();		// role : ROLE_USER 또는 ROLE_ADMIN
//			
//			// ROLE_DEVELOPER, ROLE_BOSS 등등.. 권한 추가 가능
//			List<GrantedAuthority> authorities = new ArrayList<>();
//			// ROLE_ 형식을 '반드시' 지켜야함
//			authorities.add(new SimpleGrantedAuthority(role));
//			
//			// 빈(empty) context를 하나 만들어줌.
//			SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//			
//			// 토큰(유저정보, 비밀번호, 권한:예:ADMIN)
//			AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, authorities);
//			
//			// 인증 토큰에 디테일값 추가함
//			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//			
//			// 설정
//			securityContext.setAuthentication(authenticationToken);
//			// 등록
//			SecurityContextHolder.setContext(securityContext);
//			
//			// 검증이 잘 된 경우
//			
//		} catch(Exception exception) {
//			exception.printStackTrace();
//		}
//	}
//	
//	// 토큰값 파싱해옴.
//	private String parseBearerToken(HttpServletRequest request) {
//		String authorization = request.getHeader("Authorization");
//		
//		// authorization 이 존재하지 않는다면 null값 반환하고
//		boolean hasAuthorization = StringUtils.hasText(authorization);
//		if (!hasAuthorization) return null;
//		
//		// bearer 인증 방식인지 확인하고, 아니면 null값 반환하자
//		boolean isBearer = authorization.startsWith("Bearer ");
//		if (!isBearer) return null;
//		
//		// 토큰 꺼내오기.	Bearer 뒤(빈칸뒤)부터 가져오는거라 7번부터 가져오면 됨
//		String token = authorization.substring(7);
//		return token;
//	}
//	
//}
