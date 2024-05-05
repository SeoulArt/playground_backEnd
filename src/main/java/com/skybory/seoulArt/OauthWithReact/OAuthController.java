package com.skybory.seoulArt.OauthWithReact;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/auth")
public class OAuthController {

	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
	private String naverClientId;

	@Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
	private String naverRedirectUri;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String kakaoClientId;

	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String kakaoRedirectUri;

	private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";

	@Autowired
	private OAuth2Service oAuth2Service; // OAuth2 서비스에 대한 의존성 주입

	@GetMapping("/{provider}/login-url") // 클라이언트로부터 GET 요청을 받음
	@Operation(summary = "로그인 url 요청", description = "로그인 url을 요청합니다")
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

//    @Operation(summary = "토큰 받기", description = "토큰을 요청합니다")
//    @ApiResponse(responseCode = "200", description = "요청 성공")
//    @ApiResponse(responseCode = "400", description = "요청 실패")
//    @PostMapping("/kakao/token")
//    public ResponseEntity<Object> getToken(@RequestHeader String code, @RequestBody String body) {
//
//    	// 요청 헤더 설정 : Content-Type: application/x-www-form-urlencoded
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        // 요청 바디 설정 ( 카카오 개발자 웹사이트 참고 )
//        /*
//         *   -d "grant_type=authorization_code" \
//			 -d "client_id=${REST_API_KEY}" \
//			 --data-urlencode "redirect_uri=${REDIRECT_URI}" \
//			 -d "code=${AUTHORIZE_CODE}"
//         */
//        String requestBody = String.format("grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s",
//                kakaoClientId, kakaoRedirectUri, code);
//        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Object> response = restTemplate.postForEntity(KAKAO_TOKEN_URL, request, Object.class);
//        return response;
//    }
//    @Operation(summary = "토큰 받기", description = "토큰을 요청합니다")
//    @ApiResponse(responseCode = "200", description = "요청 성공")
//    @ApiResponse(responseCode = "400", description = "요청 실패")
//    @PostMapping("/kakao/token")
//    public ResponseEntity<String> getToken(@RequestHeader("Content-Type") String contentType,
//                                           @RequestBody MultiValueMap<String, String> requestBody) {
//        // Content-Type 값 확인
//        System.out.println("Content-Type: " + contentType);
//        
//        // HTTP 요청을 위한 RestTemplate 객체 생성
//        RestTemplate restTemplate = new RestTemplate();
//        
//        // 헤더 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        // HTTP 요청 수행
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
//        ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_TOKEN_URL, requestEntity, String.class);
//
//        // 서버로부터 받은 응답을 그대로 반환
//        return response;
//    }
//}
	@Operation(summary = "토큰 받기", description = "토큰을 요청합니다")
	@ApiResponse(responseCode = "200", description = "요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 실패")
	@PostMapping("/kakao/token")
	public ResponseEntity<KakaoMemberResponse> login(@RequestBody Map<String, String> requestBody) throws Exception {

		String code = requestBody.get("code");
		OAuthProvider provider = new OAuthProvider();
		KakaoAccessTokenResponse accessTokenResponse = provider.getAccessToken(code);
		String accessToken = accessTokenResponse.getAccess_token();
		KakaoMemberResponse kakaoMemberResponse = provider.getMemberInfo(accessToken);

		// 서버로부터 받은 응답을 그대로 반환
		return ResponseEntity.ok(kakaoMemberResponse);
	}

	@Operation(summary = "토큰 받기", description = "토큰을 요청합니다")
	@ApiResponse(responseCode = "200", description = "요청 성공")
	@ApiResponse(responseCode = "400", description = "요청 실패")
	@PostMapping("/kakao/token/onlyToken")
	public ResponseEntity<KakaoAccessTokenResponse> getToken(@RequestBody Map<String, String> requestBody) throws Exception {
	    String code = requestBody.get("code");

	    OAuthProvider provider = new OAuthProvider();
	    KakaoAccessTokenResponse accessTokenResponse = provider.getAccessToken(code);

	    return ResponseEntity.ok(accessTokenResponse);
	}
}
//    private final String KAKAO_USER_URL = "https://kapi.kakao.com/v2/user/me";
//
//    @GetMapping("/kakao/user")
//    public ResponseEntity<Object> getUserData(@RequestHeader("Authorization") String authorizationHeader) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", authorizationHeader);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Object> response = restTemplate.getForEntity(KAKAO_USER_URL, Object.class);
//
//        return response;
//    }
//}
