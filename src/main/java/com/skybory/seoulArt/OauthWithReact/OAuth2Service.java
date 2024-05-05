package com.skybory.seoulArt.OauthWithReact;

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
    
    
    String authorizeUrl = "https://nid.naver.com/oauth2.0/authorize";
    String redirectUri = "https://www.seoulart.site/callback"; // 네이버로부터 리다이렉트 받을 URL
    String state = "some_random_state_value"; // CSRF 공격 방지를 위한 상태 토큰
    String scope = "profile email"; // 사용자 정보 요청 스코프

    
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
            return "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        } else {
            // 기타 처리
            return "error";
        }
        
    }
}