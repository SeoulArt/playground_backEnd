package com.skybory.seoulArt.Oauth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.skybory.seoulArt.domain.user.service.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
//    @Autowired
//    private AccessDeniedHandler accessDeniedHandler;

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
		
//		http.exceptionHandling((exceptions) -> exceptions.accessDeniedHandler(accessDeniedHandler));


		http.authorizeHttpRequests(	
				(auth) -> auth
			    .requestMatchers("/", "/api/seat/**","/api/reply/**", "/api/user/**", "/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**" , "/api/event/**", "/api/ticket/**"  ).permitAll() // /api/auth/ 하위 경로에 대한 접근을 모두 허용
//				.requestMatchers("/**").permitAll() // /api/auth/ 하위 경로에 대한 접근을 모두 허용
//			    .requestMatchers("/api/ticket/**").hasRole("USER") // /api/event/** 경로는 ADMIN 롤만 접근 가능	-> 이상하게 작동함
//			    .requestMatchers("/api/event/**").hasRole("ADMIN") // /api/event/** 경로는 ADMIN 롤만 접근 가능	-> 이상하게 작동함
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
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
 
}