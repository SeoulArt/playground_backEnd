package com.skybory.seoulArt.Oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import com.skybory.seoulArt.domain.user.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// csrf disable
		http.csrf((auth) -> auth.disable());

		// Form 로그인 방식 disable
		http.formLogin((auth) -> auth.disable());

		// HTTP Basic 인증 방식 disable
		http.httpBasic((auth) -> auth.disable());
		
		// 세션 설정 : STATELESS
		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


		http.authorizeHttpRequests(	
				(auth) -> auth
				.requestMatchers("/", "/api/auth/**" ).permitAll() // 특정 경로 무제한 접근 허용
//				.requestMatchers("/**").permitAll() // 특정 경로 무제한 접근 허용
				.anyRequest().authenticated())
		.addFilterBefore(new JwtAuthenticationFilter(userService,jwtUtil), UsernamePasswordAuthenticationFilter.class);
		
		
		return http.build();
//		// oauth2
//		http.oauth2Login((oauth2) -> oauth2.userInfoEndpoint(
//				(userInfoEndpointConfig) -> userInfoEndpointConfig.userService(customOAuth2UserService)));

		// 경로별 인가 작업 ( 원본 (0509버젼))
//		http.authorizeHttpRequests(
//				(auth) -> auth.requestMatchers("/**")
//								.permitAll()
//								.anyRequest()
//								.authenticated());
		// 경로별 인가 작업 ( 업그레이드 버젼 )
	}
 
}