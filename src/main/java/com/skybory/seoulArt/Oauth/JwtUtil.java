package com.skybory.seoulArt.Oauth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class JwtUtil {

	   private static final String SECRET_KEY = "THISISSKYBORYYUMYUMDEVELOPINGTHANKYOU";
	   private SecretKey secretKey;
	   
	    public JwtUtil() {
	        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
	        this.secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
	    }
	// JWT 생성 메서드
	public String generateToken(Long userId) {
		long now = System.currentTimeMillis();
		long expiry = 1000 * 60 * 10; // 토큰의 유효 시간은 1시간 -> 지금은 5분으로 설정해둠 (ms단위)

        return Jwts.builder()
                .claim("userId", userId) // 사용자 ID를 클레임으로 추가
                .setIssuedAt(new Date(now)) // 토큰 발행 시간
                .setExpiration(new Date(now + expiry)) // 토큰 만료 시간
                .signWith(secretKey) // 시크릿 키를 사용하여 서명
                .compact();
	}

    // JWT 토큰의 유효성 검증 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	@Bean
	public JwtDecoder jwtDecoder() {
//	    SecretKey secretKey = Keys.hmacShaKeyFor("THISISSKYBORYYUMYUMDEVELOPINGTHANKYOU".getBytes(StandardCharsets.UTF_8));
	    return NimbusJwtDecoder.withSecretKey(secretKey).build();
	}
	
    // JWT 토큰 검증 및 클레임 추출 메서드
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    
    // JWT에서 userId 추출
    public Long getUserIdFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class); // 클레임에서 userId 값을 Long 타입으로 추출
    }
}