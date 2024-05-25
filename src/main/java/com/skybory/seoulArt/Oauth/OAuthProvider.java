package com.skybory.seoulArt.Oauth;

import org.springframework.beans.factory.annotation.Value;
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
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class OAuthProvider {
	private final RestTemplate restTemplate = new RestTemplate();
	
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String naverTokenUri;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String naverUserInfoUri;

    private String kakaoLocalUri = "https://localhost:5173/oauth/callback/kakao";

    private String naverLocalUri = "https://localhost:5173/oauth/callback/naver";
    
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
            body.add("client_secret", kakaoClientSecret);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

            return restTemplate.postForEntity(
            		kakaoTokenUri, request, AccessTokenResponse.class).getBody();
        } catch(Exception e) {
        	throw new Exception(e.getMessage());
        }
    }

    public AccessTokenResponse getLocalAccessToken(String authorizationCode) throws Exception {
    	try {
    		// HTTP 요청을 위한 RestTemplate 객체 생성
    		HttpHeaders httpHeaders = new HttpHeaders();
    		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//            httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    		
    		
    		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    		body.add("grant_type", "authorization_code");
    		body.add("client_id", kakaoClientId);
    		body.add("redirect_uri", kakaoLocalUri);
    		body.add("code", authorizationCode);
    		body.add("client_secret", kakaoClientSecret);
    		
    		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
    		
    		return restTemplate.postForEntity(
    				kakaoTokenUri, request, AccessTokenResponse.class).getBody();
    	} catch(Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }

    public String getRequestURL(String code) {
        return UriComponentsBuilder.fromHttpUrl(naverTokenUri)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", naverClientId)
                .queryParam("client_secret", naverClientSecret)
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
            body.add("client_secret", naverClientSecret);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
//            RestTemplate restTemplate = new RestTemplate();
    	    System.out.println("네이버 엑세스 토큰 버젼2 종료");
    	    System.out.println("naverRedirectUri 값을 찍어보자" + naverRedirectUri);
            return restTemplate.postForEntity(
            		naverTokenUri, request, AccessTokenResponse.class).getBody();
            
    	} catch(Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }

    public AccessTokenResponse getLocalAccessTokenV2(String authorizationCode) throws Exception {
    	try {
    		System.out.println("네이버 엑세스 토큰 버젼2 실행");
    		HttpHeaders httpHeaders = new HttpHeaders();
    		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    		
    		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    		body.add("grant_type", "authorization_code");
    		body.add("client_id", naverClientId);
    		body.add("redirect_uri", naverLocalUri);
    		body.add("code", authorizationCode);
    		body.add("client_secret", naverClientSecret);
    		
    		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
//            RestTemplate restTemplate = new RestTemplate();
    		System.out.println("네이버 엑세스 토큰 버젼2 종료");
    		System.out.println("naverRedirectUri 값을 찍어보자" + naverRedirectUri);
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

                return restTemplate.postForEntity(kakaoUserInfoUri, request, KakaoMemberResponse.class)
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
    		
            return restTemplate.postForEntity(naverUserInfoUri, request, NaverMemberResponse.class)
                    .getBody();
        } catch(Exception e) {
        	throw new Exception(e.getMessage());
        }
    }
//        public AccessTokenResponse refreshToken(RefreshTokenRequest request) throws Exception {
//            try {
//                // HTTP 요청을 위한 RestTemplate 객체 생성
//                
//                HttpHeaders httpHeaders = new HttpHeaders();
//                httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//                
//                String clientId;
//                clientId = kakaoClientId;
//
//                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
////                body.add("grant_type", "authorization_code");
//                body.add("client_id", clientId);
//                body.add("grant_type", "refresh_token");
//                body.add("client_secret", kakaoClientSecret);
//                body.add("refresh_token", request.getRefresh_token());
//
//                HttpEntity<MultiValueMap<String, String>> postRequest = new HttpEntity<>(body, httpHeaders);
////                RestTemplate restTemplate = new RestTemplate();
//
//                return restTemplate.postForEntity(
//                		kakaoTokenUri, postRequest, AccessTokenResponse.class).getBody();
//            } catch(Exception e) {
//            	throw new Exception(e.getMessage());
//            }
//        }

}