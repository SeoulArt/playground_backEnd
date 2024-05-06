package com.skybory.seoulArt.OauthWithReact;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.skybory.seoulArt.domain.user.dto.UserDTO;
import com.skybory.seoulArt.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuthController {

//	private final RestTemplate restTemplate = new RestTemplate();
	private final NaverProperties naverProperties = new NaverProperties();
	OAuthProvider provider = new OAuthProvider(naverProperties);

//	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
//	private String naverClientId;
//
//	@Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
//	private String naverRedirectUri;
//
//	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
//	private String kakaoClientId;
//
//	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
//	private String kakaoRedirectUri;

//	private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";

	@Autowired
	private OAuth2Service oAuth2Service; // OAuth2 서비스에 대한 의존성 주입

	@Autowired
	private UserService userService;
//	private CustomOAuth2UserService customOAuth2UserService;


	@GetMapping("/{provider}/login-url") // 클라이언트로부터 GET 요청을 받음
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

	@Operation(summary = "통합 로그인", description = "카카오 또는 네아버 회원 정보를 가져옵니다")
	@ApiResponse(responseCode = "200", description = "요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 실패")
	@PostMapping("/{domain}/login")
	public ResponseEntity<UserDTO> kakaoLogin(@RequestBody Map<String, String> requestBody, @PathVariable("domain") String domain)
			throws Exception {

		String code = requestBody.get("code");
//		OAuthProvider provider = new OAuthProvider();

		if (domain.equals("kakao")) {

			// 액세스 토큰 받아오기
			AccessTokenResponse accessTokenResponse = provider.getKakaoAccessToken(code);
			String accessToken = accessTokenResponse.getAccess_token();
			// 토큰으로 사용자 정보 받아오기
			KakaoMemberResponse kakaoMemberResponse = provider.getMemberInfo(accessToken);
			// 아이디가 없으면 회원가입, 있우면 로그인하기
			// 서버로부터 받은 응답을 UserDTO 형태로 응답
			return ResponseEntity.ok(userService.login(kakaoMemberResponse));
		}

		if (domain.equals("naver")) {
			System.out.println("도메인 : 네이버 인식 완료");
			AccessTokenResponse accessTokenResponse = provider.getNaverAccessTokenV2(code);
			System.out.println("에세스 토큰 가져오기 성공");
			String accessToken = accessTokenResponse.getAccess_token();
			// 토큰으로 사용자 정보 받아오기
			
			NaverMemberResponse naverMemberResponse = provider.getNaverInfo(accessToken);
			System.out.println("네이버 인포 가져오기 성공");
			return ResponseEntity.ok(userService.login(naverMemberResponse));
		}
		else return null;
	}

//	@Operation(summary = "네이버 로그인", description = "인가코드로 토큰을 받아오고 회원정보를 반환합니다")
//	@ApiResponse(responseCode = "200", description = "요청 성공")
//	@ApiResponse(responseCode = "400", description = "요청 실패")
//	@PostMapping("/login/naver")
//	public NaverUserResponse.NaverUserDetail naverlogin(@RequestBody Map<String, String> requestBody) throws Exception {
//		
//		String code = requestBody.get("code");
//		// 액세스 토큰 받아오기
//		String accessToken = provider.getNaverAccessToken(code);
//		HttpHeaders headers = new HttpHeaders();
//		
//		headers.setBearerAuth(accessToken);
//		
//		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
//		
//		ResponseEntity<NaverUserResponse> response =
//				restTemplate.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, request, NaverUserResponse.class);
//		
//		
//		return response.getBody().getNaverUserDetail();
//	}

	@Operation(summary = "네이버 로그인", description = "인가코드로 토큰을 받아오고 회원정보를 반환합니다")
	@ApiResponse(responseCode = "200", description = "요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 실패")
	@PostMapping("/login/naver/v2")
	public ResponseEntity<NaverMemberResponse> naverloginV2(@RequestBody Map<String, String> requestBody)
			throws Exception {

		String code = requestBody.get("code");
		// 액세스 토큰 받아오기
		AccessTokenResponse accessTokenResponse = provider.getNaverAccessTokenV2(code);

		String accessToken = accessTokenResponse.getAccess_token();

		// 토큰으로 사용자 정보 받아오기
		NaverMemberResponse naverMemberResponse = provider.getNaverInfo(accessToken);

		// 아이디가 없으면 회원가입, 있우면 로그인하기
		// 서버로부터 받은 응답을 UserDTO 형태로 응답
		return ResponseEntity.ok(naverMemberResponse);
	}

	@PostMapping("refresh/token")
	public ResponseEntity<AccessTokenResponse> refreshToken() {

		return null;
	}

//	// 토큰만 받아오는 메서드
//	@Operation(summary = "토큰 받기", description = "토큰을 요청합니다")
//	@ApiResponse(responseCode = "200", description = "요청 성공")
//	@ApiResponse(responseCode = "400", description = "요청 실패")
//	@PostMapping("/naver")
//	public ResponseEntity<String> getNaverToken(@RequestBody Map<String, String> requestBody) throws Exception {
//		String code = requestBody.get("code");
//		return ResponseEntity.ok(provider.getNaverAccessToken(code));
//	}
//
//}

//	// 토큰만 받아오는 메서드
//	@Operation(summary = "토큰 받기", description = "토큰을 요청합니다")
//	@ApiResponse(responseCode = "200", description = "요청 성공")
//	@ApiResponse(responseCode = "400", description = "요청 실패")
//	@PostMapping("/kakao/token/onlyToken")
//	public ResponseEntity<KakaoAccessTokenResponse> getToken(@RequestBody Map<String, String> requestBody) throws Exception {
//	    String code = requestBody.get("code");
//
//	    KakaoAccessTokenResponse accessTokenResponse = provider.getKakaoAccessToken(code);
//
//	    return ResponseEntity.ok(accessTokenResponse);
}