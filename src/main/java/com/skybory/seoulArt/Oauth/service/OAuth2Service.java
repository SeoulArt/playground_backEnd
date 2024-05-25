package com.skybory.seoulArt.Oauth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OAuth2Service {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    
    @Value("${spring.security.oauth2.client.registration.naver.scope}")
    private String scope;
    
    

    
    public String buildAuthUrl(String provider) {
        String clientId;
        String redirectUri;
        
        // provider에 따라 clientId와 redirectUri 설정
        if ("kakao".equals(provider)) {
            clientId = kakaoClientId;
            redirectUri = kakaoRedirectUri;
            System.out.println("\"https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=\" + clientId + \"&redirect_uri=\" + redirectUri");
            return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
//            return redirectUri;
        } else if ("naver".equals(provider)) {
            
            clientId = naverClientId;
            redirectUri = naverRedirectUri;
            return "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope;
        } else if ("localKakao".equals(provider)) {
            
            clientId = kakaoClientId;
            redirectUri = "https://localhost:5173/oauth/callback/kakao";
            System.out.println("\"https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=\" + clientId + \"&redirect_uri=\" + redirectUri");
            return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        }  else if ("localNaver".equals(provider)) {
            
            clientId = naverClientId;
            redirectUri = "https://localhost:5173/oauth/callback/naver";
            System.out.println("\"https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=\" + clientId + \"&redirect_uri=\" + redirectUri");
            return "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        }else {
            // 기타 처리
            return "error";
        }
        
    }
    
}