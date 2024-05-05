//package com.skybory.seoulArt.Oauth_youtube;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import com.skybory.ticketing.dto.KaKaoAccessTokenResponse;
//import com.skybory.ticketing.dto.KakaoMemberResponse;
//
//import lombok.RequiredArgsConstructor;
//
//@Component
//@RequiredArgsConstructor
//public class OAuthProvider {
//
//	
//	//클라이언트 역할의 객체 생성. 클라이언트 아이디나 비밀번호 등의 상수값 포함하고있음.
//	
//    public static final String AUTHORIZATION_HEADER = "Authorization";
//    public static final String GRANT_TYPE = "authorization_code";
//    public static final String TOKEN_TYPE = "Bearer ";
//    
//    //액세스 토큰 요청, 사용자 정보 요청, 회원 탈퇴 요청 등의 처리를 할깨 필요한 restTemplate 호출해둠
//    private final RestTemplate restTemplate;
//    
//    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
//    private String tokenRequestUri;
//
//    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
//    private String memberInfoRequestUri;
//    
//    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
//    private String redirectUri;
//
//    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
//    private String clientId;
//
//    
//    // 인가 코드 요청하기 ㅇㄷ감? 이건 내가 카카오랑 하는것이기 때문에 메서드로 작성 안하는듯.
//
//    public KaKaoAuthorizationCodeResponse getAuthorizationCode() {
//    	
//    	HttpHeaders httpHeaders = new HttpHeaders();
//    	
//    	
//		return null;
//    	
//    }
//    
//    
//    // 사용자 정보 요청하기 
//    public KakaoMemberResponse getMemberInfo(String accessToken) throws KaKaoMemberInfoRequestException, KakaoServerException {
//        try {
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.set(AUTHORIZATION_HEADER, TOKEN_TYPE + accessToken);
//            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);
//
//            return restTemplate.postForEntity(memberInfoRequestUri, request, KakaoMemberResponse.class)
//                    .getBody();
//        } catch (HttpClientErrorException e) {
//            throw new KaKaoMemberInfoRequestException(e.getMessage());
//        } catch (HttpServerErrorException e) {
//            throw new KakaoServerException(e.getMessage());
//        }
//    }
//    
//    
//    // 엑세스 토큰 요청하기
//    public KaKaoAccessTokenResponse getAccessToken(String authorizationCode) throws KakaoServerException, KakaoTokenRequestException {
//        try {
//            HttpHeaders httpHeaders = new HttpHeaders();
//            
//            // 헤더의 컨텐트 타입은 'application/x-www-form-urlencoded', 카카오에서 정해줬음
//            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//            // 키는 5개 보내야하는데, grant type, client id, redirect uri, code , client secret임.
//            // 여기서 code는 인가코드인데, 이건 내가 직접 받아와야함(고객이 받는것 아님)
//            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//            body.add("grant_type", GRANT_TYPE);
//            body.add("client_id", clientId);
//            body.add("redirect_uri", redirectUri);
//            body.add("code", authorizationCode);
//
//            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);
//
//            return restTemplate.postForEntity(
//                    tokenRequestUri, request, KaKaoAccessTokenResponse.class).getBody();
//        } catch (HttpClientErrorException e) {
//            throw new KakaoTokenRequestException(e.getMessage());
//        } catch (HttpServerErrorException e) {
//            throw new KakaoServerException(e.getMessage());
//        }
//    }
//    
//    
//
//}