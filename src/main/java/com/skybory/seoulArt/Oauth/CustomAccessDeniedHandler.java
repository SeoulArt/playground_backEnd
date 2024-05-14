//package com.skybory.seoulArt.Oauth;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.log4j.Log4j2;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.time.LocalDateTime;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.skybory.seoulArt.global.exception.ApiError;
//import com.skybory.seoulArt.global.exception.ErrorCode;
//import com.skybory.seoulArt.global.exception.ServiceException;
//
//@Component
//@Log4j2
//public class CustomAccessDeniedHandler implements AccessDeniedHandler {
//
//	@Override
//	public void handle(HttpServletRequest request, HttpServletResponse response,
//			AccessDeniedException accessDeniedException) throws IOException, ServletException {
//		log.info("CustomAccessDeniedHandler 실행");
//		ApiError apiError = new ApiError(LocalDateTime.now(),ErrorCode.INVALID_TOKEN);
//		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
////		response.setContentType("application/json");
//		OutputStream out = response.getOutputStream();
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.writeValue(out, apiError);
//		out.flush();
//	}
//
//}
