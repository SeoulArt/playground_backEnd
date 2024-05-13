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

    @Autowired
    private JwtUtil jwtUtil;
	OAuthProvider oAuthProvider = new OAuthProvider();

	@Autowired
	private OAuth2Service oAuth2Service; // OAuth2 서비스에 대한 의존성 주입

	@Autowired
	private UserService userService;
//	private CustomOAuth2UserService customOAuth2UserService;


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
    public ResponseEntity<UserDTO> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        
        // refreshToken 이 없거나, validate 가 false 일 때
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
        	 throw new ServiceException(ErrorCode.INVALID_TOKEN);
        }

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        UserDTO response = userService.loadUserByUserId(userId);
        String newAccessToken = jwtUtil.generateAccessToken(userId);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId);
        HttpHeaders headers = new HttpHeaders();
        jwtUtil.setAccessToken(headers, newAccessToken);
        jwtUtil.setRefreshToken(headers, newRefreshToken);
        return ResponseEntity.ok().body(response);
    }
	
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 'authToken' 쿠키를 찾아서 만료 시키기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    cookie.setValue(""); // 쿠키의 값을 빈 문자열로 설정
                    cookie.setPath("/"); // 쿠키의 경로 설정
                    cookie.setMaxAge(0); // 쿠키의 만료 시간을 0으로 설정하여 즉시 만료
                    response.addCookie(cookie); // 응답에 쿠키 추가하여 클라이언트로 전송
                }
                if (cookie.getName().equals("refreshToken")) {
                	cookie.setValue(""); // 쿠키의 값을 빈 문자열로 설정
                	cookie.setPath("/"); // 쿠키의 경로 설정
                	cookie.setMaxAge(0); // 쿠키의 만료 시간을 0으로 설정하여 즉시 만료
                	response.addCookie(cookie); // 응답에 쿠키 추가하여 클라이언트로 전송
                }
            }
        }
        return "로그아웃 성공";
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
	
	// 카카오 로그인
	@Operation(summary = "카카오 로그인", description = "카카오 회원 정보를 가져옵니다")
	@ApiResponse(responseCode = "200", description = "요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 실패")
	@PostMapping("/kakao/login")
	public ResponseEntity<UserDTO> kakaoLogin(@RequestBody LoginRequest request) throws Exception {
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

        HttpHeaders headers = new HttpHeaders();
        jwtUtil.setAccessToken(headers, jwtAccessToken);
        jwtUtil.setRefreshToken(headers, jwtRefreshToken);
	    log.info("쿠키 저장 성공");

	    return ResponseEntity.ok().headers(headers).body(user);
	}
	
	// 네이버 로그인 메서드
	@Operation(summary = "네이버 로그인", description = "네이버 회원 정보를 가져옵니다")
	@ApiResponse(responseCode = "200", description = "요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 실패")
	@PostMapping("/naver/login")
	public ResponseEntity<UserDTO> naverLogin(@RequestBody LoginRequest request) throws Exception {
	    String code = request.getCode();
	    AccessTokenResponse accessTokenResponse = oAuthProvider.getNaverAccessTokenV2(code);
	    String accessToken = accessTokenResponse.getAccess_token();
	    NaverMemberResponse naverMemberResponse = oAuthProvider.getNaverInfo(accessToken);

	    UserDTO user = userService.login(naverMemberResponse);
	    // JWT 액세스 토큰과 리프레시 토큰 생성
	    String jwtAccessToken = jwtUtil.generateAccessToken(user.getUserId());
	    String jwtRefreshToken = jwtUtil.generateRefreshToken(user.getUserId());

        HttpHeaders headers = new HttpHeaders();
        jwtUtil.setAccessToken(headers, jwtAccessToken);
        jwtUtil.setRefreshToken(headers, jwtRefreshToken);
	    log.info("쿠키 저장 성공");

	    return ResponseEntity.ok().headers(headers).body(user);
	}
	
    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
	
}

//	@Operation(summary = "카카오 로그인", description = "인가코드로 토큰을 받아오고 회원정보를 반환합니다")
//	@ApiResponse(responseCode = "200", description = "요청 성공")
//	@ApiResponse(responseCode = "400", description = "요청 실패")
//	@PostMapping("/kakao/login")
//	public ResponseEntity<UserDTO> kakaoLogin(@RequestBody Map<String, String> requestBody) throws Exception {
//
//		String code = requestBody.get("code");
////		OAuthProvider provider = new OAuthProvider();
//
//		// 액세스 토큰 받아오기
//		AccessTokenResponse accessTokenResponse = provider.getKakaoAccessToken(code);
//		String accessToken = accessTokenResponse.getAccess_token();
//
//		// 토큰으로 사용자 정보 받아오기
//		KakaoMemberResponse kakaoMemberResponse = provider.getMemberInfo(accessToken);
////		customOAuth2UserService.loadUser(null);
//
//		// 아이디가 없으면 회원가입, 있우면 로그인하기
//		// 서버로부터 받은 응답을 UserDTO 형태로 응답
//		return ResponseEntity.ok(userService.login(kakaoMemberResponse));
//	}

//	@GetMapping("/generate-token")
//    public String generateToken(@RequestParam Long userId) {
//        return jwtUtil.generateToken(userId);
//    }
//
//	@GetMapping("/validate-token")
//	public boolean validateToken(@RequestParam String token) {
//		return jwtUtil.validateToken(token);
//	}

//	@Operation(summary = "네이버 로그인", description = "인가코드로 토큰을 받아오고 회원정보를 반환합니다")
//	@ApiResponse(responseCode = "200", description = "요청 성공")
//	@ApiResponse(responseCode = "400", description = "요청 실패")
//	@PostMapping("/login/naver/v2")
//	public ResponseEntity<NaverMemberResponse> naverloginV2(@RequestBody Map<String, String> requestBody)
//			throws Exception {
//
//		String code = requestBody.get("code");
//		// 액세스 토큰 받아오기
//		AccessTokenResponse accessTokenResponse = provider.getNaverAccessTokenV2(code);
//
//		String accessToken = accessTokenResponse.getAccess_token();
//
//		// 토큰으로 사용자 정보 받아오기
//		NaverMemberResponse naverMemberResponse = provider.getNaverInfo(accessToken);
//
//		// 아이디가 없으면 회원가입, 있우면 로그인하기
//		// 서버로부터 받은 응답을 UserDTO 형태로 응답
//		return ResponseEntity.ok(naverMemberResponse);
//	}


//	@Operation(summary = "통합 로그인", description = "카카오 또는 네이버 회원 정보를 가져옵니다")
//	@ApiResponse(responseCode = "200", description = "요청 성공")
//	@ApiResponse(responseCode = "400", description = "요청 실패")
////	@PostMapping("/{domain}/login")
//	public ResponseEntity<UserDTO> kakaoLogin(@RequestBody LoginRequest request, @PathVariable("provider") String provider)
//			throws Exception {
//
//		String code = request.getCode();
////		OAuthProvider provider = new OAuthProvider();
//
//		if (provider.equals("kakao")) {
//
//			// 액세스 토큰 받아오기
//			AccessTokenResponse accessTokenResponse = oAuthProvider.getKakaoAccessToken(code);
//			String accessToken = accessTokenResponse.getAccess_token();
//			
//			// 토큰으로 사용자 정보 받아오기
//			KakaoMemberResponse kakaoMemberResponse = oAuthProvider.getMemberInfo(accessToken);
//			// 아이디가 없으면 회원가입, 있우면 로그인하기
//			// 서버로부터 받은 응답을 UserDTO 형태로 응답
//			return ResponseEntity.ok(userService.login(kakaoMemberResponse));
//		}
//
//		if (provider.equals("naver")) {
//			System.out.println("도메인 : 네이버 인식 완료");
//			AccessTokenResponse accessTokenResponse = oAuthProvider.getNaverAccessTokenV2(code);
//			System.out.println("에세스 토큰 가져오기 성공");
//			String accessToken = accessTokenResponse.getAccess_token();
//			// 토큰으로 사용자 정보 받아오기
//			
//			NaverMemberResponse naverMemberResponse = oAuthProvider.getNaverInfo(accessToken);
//			System.out.println("네이버 인포 가져오기 성공");
//			return ResponseEntity.ok(userService.login(naverMemberResponse));
//		}
//		else return null;
//	}