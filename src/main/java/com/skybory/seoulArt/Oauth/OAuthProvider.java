package com.skybory.seoulArt.Oauth;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.skybory.seoulArt.Oauth.dto.AccessTokenResponse;
import com.skybory.seoulArt.Oauth.dto.KakaoMemberResponse;
import com.skybory.seoulArt.Oauth.dto.NaverMemberResponse;
import com.skybory.seoulArt.Oauth.dto.RefreshTokenRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthProvider {
	RestTemplate restTemplate = new RestTemplate();
//	private final NaverProperties naverProperties;
//	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId = "506825a69a9d9a83db0f7f1f8aaffe7d";

//    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientPw = "Flejpx0RTQrq2fvNfbyENG0JWfFpqUwh";

//    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri = "http://localhost:5173/oauth/callback/kakao";
    
//    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userUri = "https://kapi.kakao.com/v2/user/me";
    
//    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri = "https://kauth.kakao.com/oauth/token";
    
    private String naverClientId = "EFSiWGS4Omrh4Tv4ZHgm";
    
    private String naverClientPw = "TpgP7auqTu";
    
    private String naverRedirectUri = "http://localhost:5173/oauth/callback/naver";
    
    private String naverUserUri = "https://openapi.naver.com/v1/nid/me";
    
    private String naverTokenUri = "https://nid.naver.com/oauth2.0/token";
//    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    
    public AccessTokenResponse getKakaoAccessToken(String authorizationCode) throws Exception {
        try {
            // HTTP 요청을 위한 RestTemplate 객체 생성
            
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//            httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", kakaoClientId);
            body.add("redirect_uri", kakaoRedirectUri);
            body.add("code", authorizationCode);
            body.add("client_secret", kakaoClientPw);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
//            RestTemplate restTemplate = new RestTemplate();

            return restTemplate.postForEntity(
            		tokenUri, request, AccessTokenResponse.class).getBody();
        } catch(Exception e) {
        	throw new Exception(e.getMessage());
        }
    }

    public String getRequestURL(String code) {
        return UriComponentsBuilder.fromHttpUrl(naverTokenUri)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", naverClientId)
                .queryParam("client_secret", naverClientPw)
                .queryParam("code", code)
                .toUriString();
    }

    
    public AccessTokenResponse getNaverAccessTokenV2(String authorizationCode) throws Exception {
    	try {
    	    System.out.println("네이버 엑세스 토큰 버젼2 실행");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", naverClientId);
            body.add("redirect_uri", naverRedirectUri);
            body.add("code", authorizationCode);
            body.add("client_secret", naverClientPw);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
//            RestTemplate restTemplate = new RestTemplate();
    	    System.out.println("네이버 엑세스 토큰 버젼2 종료");
            return restTemplate.postForEntity(
            		naverTokenUri, request, AccessTokenResponse.class).getBody();
            
    	} catch(Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    

        public KakaoMemberResponse getMemberInfo(String accessToken) throws Exception {
            try {
            	// 이 코드는 최상단으로 올려도 될것같음
            	RestTemplate restTemplate = new RestTemplate();
                HttpHeaders httpHeaders = new HttpHeaders();
                // 헤더에 토큰 입력
                httpHeaders.set("Authorization", "Bearer "+ accessToken);
                httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

                return restTemplate.postForEntity(userUri, request, KakaoMemberResponse.class)
                        .getBody();
            } catch(Exception e) {
            	throw new Exception(e.getMessage());
            }
        }
        
        public NaverMemberResponse getNaverInfo(String accessToken) throws Exception {
        	try {
        	// 토큰으로 사용자 정보 받아오기
        		System.out.println("네이버 인포 정보 가져오기");
        	RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer "+ accessToken);
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

            System.out.println("네이버 인포 가져오기 종료");
    		
            return restTemplate.postForEntity(naverUserUri, request, NaverMemberResponse.class)
                    .getBody();
        } catch(Exception e) {
        	throw new Exception(e.getMessage());
        }
    }
        public AccessTokenResponse refreshToken(RefreshTokenRequest request) throws Exception {
            try {
                // HTTP 요청을 위한 RestTemplate 객체 생성
                
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                
                String clientId;
                clientId = kakaoClientId;

                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//                body.add("grant_type", "authorization_code");
                body.add("client_id", clientId);
                body.add("grant_type", "refresh_token");
                body.add("client_secret", kakaoClientPw);
                body.add("refresh_token", request.getRefresh_token());

                HttpEntity<MultiValueMap<String, String>> postRequest = new HttpEntity<>(body, httpHeaders);
//                RestTemplate restTemplate = new RestTemplate();

                return restTemplate.postForEntity(
                		tokenUri, postRequest, AccessTokenResponse.class).getBody();
            } catch(Exception e) {
            	throw new Exception(e.getMessage());
            }
        }

}