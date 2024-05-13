//package com.eyy.test.jwt;
//
//
//import com.eyy.test.dto.JwtTokenDTO;
//import com.eyy.test.dto.UserInfo;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//public class JWTProvider {
//
//    private final Key key;
//
//    public JWTProvider(@Value("${jwt.secret-key}") String secretKey) {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public JwtTokenDTO generateToken(Authentication authentication) {
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        long now = System.currentTimeMillis();
//
//        // Access Token 생성
//        Date accessTokenExpiresIn = new Date(now + 86400000); // 1일 유효 기간
//        String jwtAccessToken = Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim("auth", authorities)
//                .setExpiration(accessTokenExpiresIn)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        // Refresh Token 생성
//        Date refreshTokenExpiresIn = new Date(now + 2592000000L); // 30일 유효 기간 (예시)
//        String refreshToken = Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim("auth", authorities)
//                .setExpiration(refreshTokenExpiresIn)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        return JwtTokenDTO.builder()
//                .grantType("Bearer")
//                .jwtAccessToken(jwtAccessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
//
//    // 수정된 getAuthentication 메서드
//    public Authentication getAuthentication(UserInfo userInfo) {
//        // UserInfo 객체에서 권한 정보 추출
//        List<GrantedAuthority> authorities = userInfo.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                .collect(Collectors.toList());
//
//        // UserDetails 객체 생성
//        UserDetails userDetails = new User(userInfo.getEmail(), "", authorities);
//
//        // UsernamePasswordAuthenticationToken 반환
//        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
//    }
//
//    public boolean validateToken(String jwtAccessToken) {
//        try {
//            Jwts.parser().setSigningKey(key).parseClaimsJws(jwtAccessToken);
//            return true;
//        } catch (JwtException e) {
//            log.error("Invalid JWT Token", e);
//            return false;
//        }
//    }
//
//    public Claims parseClaims(String jwtAccessToken) {
//        try {
//            return Jwts.parser().setSigningKey(key).parseClaimsJws(jwtAccessToken).getBody();
//        } catch (JwtException e) {
//            log.error("JWT parsing error", e);
//            return null;
//        }
//    }
//}