//package com.skybory.seoulArt.global.OAuth;
//
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import com.skybory.seoulArt.domain.user.User;
//import com.skybory.seoulArt.domain.user.UserRepository;
//
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//    //DefaultOAuth2UserService OAuth2UserService의 구현체
//
//	private UserRepository userRepository;
//
//
//	public CustomOAuth2UserService(UserRepository userRepository) {
//		this.userRepository = userRepository;
//	}
//	
//	
//    @Override		// 네이버, 카카오톡의 유저 정보를 받아오는 메서드 (반환형이 OAuth2User)
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//
//    	// 부모 클래스에 있는 loadUser 메서드를 사용해서 유저 정보를 가져옴.
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        // 확인용 출력문
//        System.out.println(oAuth2User.getAttributes());
//
//        // 네이버인지, 카카오톡인지 가져오는 문자열
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        OAuth2Response oAuth2Response = null;
//        if (registrationId.equals("naver")) {
//        	// 네이버인 경우
//            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
//        }	// 카카오인 경우
//        else if (registrationId.equals("kakao")) {
//
//            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
//        }
//        else {
//
//        	// 아무것도 아닌 경우
//            return null;
//        }
//        // 제공자 + 띄어쓰기 + 아이디값 으로 유저네임 스트링값 만들고
//        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
//        
//        // 해당 유저네임으로 값 있는지 확인
//        User existData = userRepository.findByUsername(username);
//        
//        String role = null;
//        
//        // 첫 번째 로그인
//        if(existData == null) {
//        	User userEntity = new User();
//        	userEntity.setUsername(username);
//        	userEntity.setEmail(oAuth2Response.getEmail());
//        	userEntity.setRole("ROLE_USER");
//        	
//        	userRepository.save(userEntity);
//        	
//        } 
//        else if (username.equals("kakao 3432634754")) {
//        	// 김태연 아이디에 대해서는 admin 권한 줌
//        	existData.setRole("ROLE_ADMIN");
//        	userRepository.save(existData);
//        }
//        // 두 번 이상 로그인 // 업데이트 안됨. 수정 필요 ========================================= userRepository.save(userEntity) 추가하거나 userRepository.update(userEntity)해야함
//        else {
//        	role = existData.getRole();
//        	existData.setEmail(oAuth2Response.getEmail());
//        	userRepository.save(existData);
//        }
//        
//        return new CustomOAuth2User(oAuth2Response, role);
//    }
//}