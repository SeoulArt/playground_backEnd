package com.skybory.seoulArt.Oauth.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.skybory.seoulArt.Oauth.JwtUtil;
import com.skybory.seoulArt.Oauth.OAuthProvider;
import com.skybory.seoulArt.Oauth.dto.AccessTokenResponse;
import com.skybory.seoulArt.Oauth.dto.KakaoMemberResponse;
import com.skybory.seoulArt.Oauth.dto.LoginRequest;
import com.skybory.seoulArt.Oauth.dto.NaverMemberResponse;
import com.skybory.seoulArt.Oauth.dto.UrlResponse;
import com.skybory.seoulArt.Oauth.service.OAuth2Service;
import com.skybory.seoulArt.domain.user.dto.LoginResponse;
import com.skybory.seoulArt.domain.user.dto.UserDTO;
import com.skybory.seoulArt.domain.user.service.UserService;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuthController {


    private final JwtUtil jwtUtil;
    private final OAuthProvider oAuthProvider;
    private final OAuth2Service oAuth2Service;
    private final UserService userService;

	@GetMapping("/{provider}/url") // 클라이언트로부터 GET 요청을 받음		-> provider 삭제
	@Operation(summary = "로그인 url 요청", description = "카카오, 또는 네이버 로그인 url을 요청합니다")
	@ApiResponse(responseCode = "200", description = "요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 실패")
	public ResponseEntity<UrlResponse> getLoginUrl(@PathVariable("provider") String provider) {
		// provider에 따라 해당 소셜 로그인 공급자의 OAuth 2.0 인증 URL 생성
		String loginUrl = oAuth2Service.buildAuthUrl(provider);
		UrlResponse response = new UrlResponse(loginUrl);
		// 생성된 인증 URL을 클라이언트에게 반환
		System.out.println("loginURL : " + loginUrl);
		return ResponseEntity.ok(response);
	}

	
	@PostMapping("/refresh")			
	@Operation(summary = "토큰 갱신하기", description = "토큰을 갱신합니다")
	@ApiResponse(responseCode = "200", description = "요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 실패")
    public ResponseEntity<LoginResponse> refreshAccessToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
	    if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
	        throw new ServiceException(ErrorCode.INVALID_TOKEN);
	    }
	    String refreshToken = bearerToken.substring(7);  // "Bearer " 후의 문자열을 추출
	    log.info("헤더에서 refreshToken 추출: " + refreshToken);
//        String refreshToken = extractRefreshTokenFromCookie(request);
//        log.info("쿠키에서 refreshToken 추출 : " + refreshToken );
        // refreshToken 이 없거나, validate 가 false 일 때
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
        	 throw new ServiceException(ErrorCode.INVALID_TOKEN);
        }

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        UserDTO user = userService.loadUserByUserId(userId);
        String jwtAccessToken = jwtUtil.generateAccessToken(userId);
        String jwtRefreshToken = jwtUtil.generateRefreshToken(userId);
	    LoginResponse response = new LoginResponse();
	    response.setUser(user);
	    response.setAccessToken(jwtAccessToken);
	    response.setRefreshToken(jwtRefreshToken);
//        HttpHeaders headers = new HttpHeaders();
//        jwtUtil.setAccessToken(headers, newAccessToken);
//        jwtUtil.setRefreshToken(headers, newRefreshToken);
//        log.info("새로운 accessToken : " + newAccessToken);
//        log.info("새로운 refreshToken : " + newRefreshToken);
        return ResponseEntity.ok().body(response);
    }
	
//    @PostMapping("/logout")
//	@Operation(summary = "로그아웃", description = "쿠키 값을 비워냅니다")
//	@ApiResponse(responseCode = "200", description = "요청 성공")
//	@ApiResponse(responseCode = "400", description = "요청 실패")
//    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
//    	log.info("로그아웃 메서드 실행");
//        log.info("로그아웃 메서드 실행");
//        HttpHeaders headers = jwtUtil.expireCookies();
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body("로그아웃 성공");
//    }

	
	// 카카오 로그인
	@Operation(summary = "카카오 로그인", description = "카카오 회원 정보를 가져옵니다")
	@ApiResponse(responseCode = "200", description = "요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 실패")
	@PostMapping("/kakao/login")
	public ResponseEntity<LoginResponse> kakaoLogin(@RequestBody LoginRequest request) throws Exception {
	    // 인가코드 받기
	    String code = request.getCode();
	    // 카카오가 엑세스 토큰을 발급받음
	    AccessTokenResponse accessTokenResponse = oAuthProvider.getKakaoAccessToken(code);
	    System.out.println("카카오 토큰 받아오기 성공");
	    String accessToken = accessTokenResponse.getAccess_token();
	    // 카카오 멤버 정보 반환
	    KakaoMemberResponse kakaoMemberResponse = oAuthProvider.getMemberInfo(accessToken);
	    System.out.println("회원 정보 받아오기 성공");

	    // 유저 DTO에 카카오 멤버 정보 매핑
	    UserDTO user = userService.login(kakaoMemberResponse);

	    // JWT 액세스 토큰과 리프레시 토큰 생성
	    String jwtAccessToken = jwtUtil.generateAccessToken(user.getUserId());
	    String jwtRefreshToken = jwtUtil.generateRefreshToken(user.getUserId());

	    LoginResponse response = new LoginResponse();
	    response.setUser(user);
	    response.setAccessToken(jwtAccessToken);
	    response.setRefreshToken(jwtRefreshToken);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + jwtAccessToken); // 액세스 토큰을 헤더에 추가
//        headers.set("Refresh-Token", jwtRefreshToken); // 리프레시 토큰을 헤더에 추가

//        log.info("헤더에 토큰 추가 성공");
//        jwtUtil.setAccessToken(headers, jwtAccessToken);
//        jwtUtil.setRefreshToken(headers, jwtRefreshToken);
        
//	    log.info("쿠키 저장 성공");

//	    return ResponseEntity.ok().headers(headers).body(user);
	    return ResponseEntity.ok().body(response);
	}

	// 카카오 로그인
	@Operation(summary = "카카오 로그인", description = "카카오 회원 정보를 가져옵니다")
	@ApiResponse(responseCode = "200", description = "요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 실패")
	@PostMapping("/local/kakao/login")
	public ResponseEntity<LoginResponse> localLogin(@RequestBody LoginRequest request) throws Exception {
		// 인가코드 받기
		String code = request.getCode();
		// 카카오가 엑세스 토큰을 발급받음
		AccessTokenResponse accessTokenResponse = oAuthProvider.getLocalAccessToken(code);
		System.out.println("로컬 토큰 받아오기 성공");
		String accessToken = accessTokenResponse.getAccess_token();
		// 카카오 멤버 정보 반환
		KakaoMemberResponse kakaoMemberResponse = oAuthProvider.getMemberInfo(accessToken);
		System.out.println("로컬 정보 받아오기 성공");
		
		// 유저 DTO에 카카오 멤버 정보 매핑
		UserDTO user = userService.login(kakaoMemberResponse);
		
		// JWT 액세스 토큰과 리프레시 토큰 생성
		String jwtAccessToken = jwtUtil.generateAccessToken(user.getUserId());
		String jwtRefreshToken = jwtUtil.generateRefreshToken(user.getUserId());
	    LoginResponse response = new LoginResponse();
	    response.setUser(user);
	    response.setAccessToken(jwtAccessToken);
	    response.setRefreshToken(jwtRefreshToken);
//		HttpHeaders headers = new HttpHeaders();
//	    headers.set("Authorization", "Bearer " + jwtAccessToken); // 액세스 토큰을 헤더에 추가
//	    headers.set("Refresh-Token", jwtRefreshToken); // 리프레시 토큰을 헤더에 추가

//	    log.info("헤더에 토큰 추가 성공");
//		jwtUtil.setAccessToken(headers, jwtAccessToken);
//		jwtUtil.setRefreshToken(headers, jwtRefreshToken);
		log.info("쿠키 저장 성공");
		
		return ResponseEntity.ok().body(response);
	}
	
	// 네이버 로그인 메서드
	@Operation(summary = "네이버 로그인", description = "네이버 회원 정보를 가져옵니다")
	@ApiResponse(responseCode = "200", description = "요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 실패")
	@PostMapping("/naver/login")
	public ResponseEntity<LoginResponse> naverLogin(@RequestBody LoginRequest request) throws Exception {
	    String code = request.getCode();
	    AccessTokenResponse accessTokenResponse = oAuthProvider.getNaverAccessTokenV2(code);
	    String accessToken = accessTokenResponse.getAccess_token();
	    NaverMemberResponse naverMemberResponse = oAuthProvider.getNaverInfo(accessToken);

	    UserDTO user = userService.login(naverMemberResponse);
	    // JWT 액세스 토큰과 리프레시 토큰 생성
	    String jwtAccessToken = jwtUtil.generateAccessToken(user.getUserId());
	    String jwtRefreshToken = jwtUtil.generateRefreshToken(user.getUserId());
//
//        HttpHeaders headers = new HttpHeaders();
//        jwtUtil.setAccessToken(headers, jwtAccessToken);
//        jwtUtil.setRefreshToken(headers, jwtRefreshToken);
	    LoginResponse response = new LoginResponse();
	    response.setUser(user);
	    response.setAccessToken(jwtAccessToken);
	    response.setRefreshToken(jwtRefreshToken);
	    log.info("쿠키 저장 성공");

	    return ResponseEntity.ok().body(response);
	}

	// 네이버 로그인 메서드
	@Operation(summary = "네이버 로그인", description = "네이버 회원 정보를 가져옵니다")
	@ApiResponse(responseCode = "200", description = "요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 실패")
	@PostMapping("/local/naver/login")
	public ResponseEntity<LoginResponse> localNaverLogin(@RequestBody LoginRequest request) throws Exception {
		String code = request.getCode();
		AccessTokenResponse accessTokenResponse = oAuthProvider.getNaverAccessTokenV2(code);
		String accessToken = accessTokenResponse.getAccess_token();
		NaverMemberResponse naverMemberResponse = oAuthProvider.getNaverInfo(accessToken);
		
		UserDTO user = userService.login(naverMemberResponse);
		// JWT 액세스 토큰과 리프레시 토큰 생성
		String jwtAccessToken = jwtUtil.generateAccessToken(user.getUserId());
		String jwtRefreshToken = jwtUtil.generateRefreshToken(user.getUserId());
		
//		HttpHeaders headers = new HttpHeaders();
//		jwtUtil.setAccessToken(headers, jwtAccessToken);
//		jwtUtil.setRefreshToken(headers, jwtRefreshToken);
	    LoginResponse response = new LoginResponse();
	    response.setUser(user);
	    response.setAccessToken(jwtAccessToken);
	    response.setRefreshToken(jwtRefreshToken);
//		log.info("쿠키 저장 성공");
//		
		return ResponseEntity.ok().body(response);
	}
	
//    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("refreshToken".equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
	
}
