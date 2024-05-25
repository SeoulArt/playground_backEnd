package com.skybory.seoulArt.Oauth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.skybory.seoulArt.domain.user.entity.User;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

@Configuration
@Service
public class JwtUtil {


	@Value("${seoulArt.secret-key}")
	private String secretKeyString;

	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
		this.secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
	}

	// AccessToken 생성 메서드
	public String generateAccessToken(Long userId) {
		long now = System.currentTimeMillis();
//        long expiry = 1000 * 60 * 1; // AccessToken 유효 시간은  (ms단위) -> 5분
		long expiry = 1000L * 60 * 60; // AccessToken 유효 시간은 1시간 (ms단위)

		return Jwts.builder().claim("userId", userId) // 사용자 ID를 클레임으로 추가
				.setIssuedAt(new Date(now)) // 토큰 발행 시간
				.setExpiration(new Date(now + expiry)) // 토큰 만료 시간
				.signWith(secretKey) // 시크릿 키를 사용하여 서명
				.compact();
	}

	// RefreshToken 생성 메서드
	public String generateRefreshToken(Long userId) {
		long now = System.currentTimeMillis();
//        long expiry = 1000L * 60 * 3; // RefreshToken 유효 시간은 1개월 (ms단위) -> 3분
		long expiry = 1000L * 60 * 60 * 24 * 30; // RefreshToken 유효 시간은 1개월 (ms단위)

		return Jwts.builder().claim("userId", userId) // 사용자 ID를 클레임으로 추가
				.setIssuedAt(new Date(now)) // 토큰 발행 시간
				.setExpiration(new Date(now + expiry)) // 토큰 만료 시간
				.signWith(secretKey) // 시크릿 키를 사용하여 서명
				.compact();
	}

	// RefreshToken 유효성 검증 및 새 AccessToken 발급 메서드
	public String refreshToken(String refreshToken) {
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(refreshToken)
					.getBody();
			Long userId = claims.get("userId", Long.class);
			return generateAccessToken(userId);
		} catch (ExpiredJwtException e) {
			throw new RuntimeException("Refresh Token has expired", e);
		} catch (Exception e) {
			throw new RuntimeException("Invalid Refresh Token", e);
		}
	}

	// JWT 토큰의 유효성 검증 메서드
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withSecretKey(secretKey).build();
	}

	// JWT 토큰 검증 및 클레임 추출 메서드
	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}

	// JWT에서 userId 추출
	public Long getUserIdFromToken(String token) {
		Claims claims = extractAllClaims(token);
		return claims.get("userId", Long.class); // 클레임에서 userId 값을 Long 타입으로 추출
	}
	
 

//    public void setAccessToken(HttpHeaders headers, String accessToken) {
//        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
//                .httpOnly(true)
//                .secure(true)  // HTTPS 환경에서는 true로 설정
//                .path("/")
//                .maxAge(1000L * 60 * 60) // 1시간
//                .sameSite("None") // CSRF protection
//                .build();
//        headers.add("Set-Cookie", accessTokenCookie.toString());
//    }

//    public void setRefreshToken(HttpHeaders headers, String jwtRefreshToken) {
//        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", jwtRefreshToken)
//                .httpOnly(true)
//                .secure(true)  // HTTPS 환경에서는 true로 설정
//                .path("/")
//                .maxAge(1000L * 60 * 60 * 24 * 30) // 쿠키의 유효기간을 refreshToken과 동일하게 설정
//                .sameSite("None")
////                .sameSite("Lax")
//                .build();
//        headers.add("Set-Cookie", refreshTokenCookie.toString());
//    }
    
//    public HttpHeaders expireCookies() {
//        HttpHeaders headers = new HttpHeaders();
//
//        // accessToken 쿠키를 만료시키기
//        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
//                .httpOnly(true)
//                .secure(true)  // HTTPS 환경에서는 true로 설정
//                .path("/")
//                .maxAge(0)  // 쿠키의 만료 시간을 0으로 설정하여 즉시 만료
//                .sameSite("None")  // 모든 요청에서 쿠키를 전송하도록 설정
//                .build();
//        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
//
//        // refreshToken 쿠키를 만료시키기
//        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
//                .httpOnly(true)
//                .secure(true)  // HTTPS 환경에서는 true로 설정
//                .path("/")
//                .maxAge(0)  // 쿠키의 만료 시간을 0으로 설정하여 즉시 만료
//                .sameSite("None")  // 모든 요청에서 쿠키를 전송하도록 설정
//                .build();
//        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
//
//        return headers;
//    }
}