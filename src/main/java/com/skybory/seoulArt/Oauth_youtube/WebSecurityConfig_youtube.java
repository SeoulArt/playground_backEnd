//package com.skybory.seoulArt.Oauth_youtube;
//
//import java.io.IOException;
//
//
//
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import com.nimbusds.oauth2.sdk.AuthorizationResponse;
//import com.nimbusds.oauth2.sdk.auth.JWTAuthentication;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//
//// 유튜브 강의 참고
//@Configurable // @BEAN 쓸수있게 해줌
//@Configuration // 설정 -> 빈에 등록
//@EnableWebSecurity // 웹 시큐리티 설정 맞추기 어노테이션
//@RequiredArgsConstructor // 제어역전
//public class WebSecurityConfig_youtube {
//
//	private final JwtAuthenticationFilter jwtAuthenticationFilter;
//	private final DefaultOAuth2UserService oAuth2UserService;
//	private final OAuth2SuccessHandler oAuth2SuccessHandler;
//	@Bean
//    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{
//    	
//    	httpSecurity
//    		.cors(cors -> cors
//    			.configurationSource(corsConfigurationSource())
//    		)
//    		.csrf(CsrfConfigurer::disable)
//    		.httpBasic(HttpBasicConfigurer::disable)
//    		.sessionManagement(sessionManagement -> sessionManagement
//    				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//    		)
//    		.authorizeHttpRequests(request -> request
//    				.requestMatchers("/", "/api/v1/auth/**").permitAll()
//    				.requestMatchers("/api/v1/user/**").hasRole("USER")
//    				.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
//    				.anyRequest().authenticated()
//    		)
//    		.oauth2Login(oauth2 -> oauth2
//    				.authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2"))
//    				.redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
//    				.userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
//    				.successHandler(oAuth2SuccessHandler)
//    		)
//    		
//    		.exceptionHandling(exceptionHandling -> exceptionHandling
//    			.authenticationEntryPoint(new FailedAuthenticationEntryPoint())
//    		)
//    		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//    		
//    	return httpSecurity.build();
//    
//    }
//
//	@Bean
//	protected CorsConfigurationSource corsConfigurationSource() {
//
//		CorsConfiguration corsConfiguration = new CorsConfiguration();
//		corsConfiguration.addAllowedOrigin("*");
//		corsConfiguration.addAllowedMethod("*");
//		corsConfiguration.addAllowedHeader("*");
//
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", corsConfiguration);
//
//		return source;
//
//	}
//
//}
//
//class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//	@Override
//	public void commence(HttpServletRequest request, HttpServletResponse response,
//			AuthenticationException authException) throws IOException, ServletException {
//		
//		response.setContentType("application/json");
//		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//		// {"code" : "NP", "message" : "No Permission."}
//		response.getWriter().write("{\"code\" : \"NP\", \"message\" : \"No Permission.\"}");
//	}
//	
//}
//// http.authorizeHttpRequests(auth -> auth
////            .requestMatchers("/", "/login").permitAll()   //모든 사용자가 접근 가능
////            .requestMatchers("/admin").hasRole("ADMIN")     // admin만 사용 가능
////            .requestMatchers("/my_page/**").hasAnyRole("ADMIN", "USER") .anyRequest().authenticated()
////            );
////
////
////        return http.build();
