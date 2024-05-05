//package com.skybory.seoulArt.OauthWithReact;
//
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//public class Kakao {
//
//	// 프론트에서 전달받는 인가코드
//    private final String key;
//    
//    // 리다이렉트 uri값
//    private final String redirectUri;
//
//    public Kakao(String key, String redirectUri) {
//        this.key = key;
//        this.redirectUri = redirectUri;
//    }
//
//    /**
//     * @description 토큰 발급하기
//     * @param code 인가코드
//     * @return 토큰 데이터
//     */
//    public Token getToken(String code) {
//        // 필수 parameter 설정
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        
//        // client id 는 RestAPI key 값으로 설정
//        params.add("client_id", key);
//        // Code 값은 프론트에서 받은 code 값으로 설정
//        params.add("code", code);
//        // grant_type 은 "authorization_code" 로 고정.
//        params.add("grant_type", "authorization_code");
//        // redirect uri 는 yml 에서 지정해둔 값으로 설정
//        params.add("redirect_uri", redirectUri);
//
//        // 헤더 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        // HTTP 요청 생성
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
//
//        // REST 호출
//        ResponseEntity<Token> responseEntity = new RestTemplate().postForEntity(
//            "https://kauth.kakao.com/oauth/token",
//            requestEntity,
//            Token.class
//        );
//
//        // 응답 확인
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            return responseEntity.getBody();
//        } else {
//            throw new RuntimeException("Failed to retrieve token from Kakao");
//        }
//    }
//
// 
//}