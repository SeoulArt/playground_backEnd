package com.skybory.seoulArt.OauthWithReact;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthProvider {

//    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId = "506825a69a9d9a83db0f7f1f8aaffe7d";

//    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientPw = "Flejpx0RTQrq2fvNfbyENG0JWfFpqUwh";

//    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri = "http://localhost:5173/oauth/callback/kakao";
    
//    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userUri = "https://kapi.kakao.com/v2/user/me";
    
//    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri = "https://kauth.kakao.com/oauth/token";
    
//    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    
    public KakaoAccessTokenResponse getAccessToken(String authorizationCode) throws Exception {
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
            RestTemplate restTemplate = new RestTemplate();

            return restTemplate.postForEntity(
            		tokenUri, request, KakaoAccessTokenResponse.class).getBody();
        } catch(Exception e) {
        	throw new Exception(e.getMessage());
        }
    }
    

        public KakaoMemberResponse getMemberInfo(String accessToken) throws Exception {
            try {
            	RestTemplate restTemplate = new RestTemplate();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set("Authorization", "Bearer "+ accessToken);
                httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

                return restTemplate.postForEntity(userUri, request, KakaoMemberResponse.class)
                        .getBody();
            } catch(Exception e) {
            	throw new Exception(e.getMessage());
            }
        }

}