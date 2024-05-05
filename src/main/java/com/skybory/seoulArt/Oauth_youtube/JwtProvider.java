//package com.skybory.seoulArt.Oauth_youtube;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class JwtProvider {		// jwt.io 사이트에 들어가보면, 인코딩 디코딩이 나옴
//	
//	@Value("${Secret-key}")
//	private String secretKey;
//	
//	// JWT를 만드는 메서드로 사용
//	public String create(String userId) {
//		
//		// 만료 기간 : 1시간으로 설정
//		Date expiredDate = Date.from(Instant.now().plus(1,ChronoUnit.HOURS));
//		
//		// secret key 만드는 작업(yml 참고)
//		Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
//	
//		// jwt 를 생성하는 코드
//		String jwt = Jwts.builder()
//				.signWith(key, SignatureAlgorithm.HS256)
//				.setSubject(userId).setIssuedAt(new Date()).setExpiration(expiredDate)
//				.compact();
//		
//		return jwt;
//	}
//	
//	// jwt를 검증하는 메서드
//	public String validate (String jwt) {
//	
//		String subject = null;
//		Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
//		
//		try {
//			subject = Jwts.parserBuilder()
//				.setSigningKey(key)
//				.build()
//				.parseClaimsJws(jwt)
//				.getBody()
//				.getSubject();
//			
//		}catch(Exception exception) {
//			exception.printStackTrace();
//			return null;
//		}
//		return subject;
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//}
