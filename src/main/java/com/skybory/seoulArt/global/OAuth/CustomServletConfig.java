//package com.skybory.seoulArt.global.OAuth;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import lombok.extern.log4j.Log4j2;
//
//@Configuration
//@Log4j2
//public class CustomServletConfig implements WebMvcConfigurer{
//
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**")				//  어느 경로에 Cors를 적용할지? /** == 모든 경로에 대해 적용
//				.maxAge(500)					//	서버의 연결 유효시간
//				.allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
// 				.allowedOrigins("*");			//  어느 경로에서 들어오는 것을 허용하는지? * == 모든 경로에 대해 적용
//	}	
//}