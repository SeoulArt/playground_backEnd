//package com.skybory.seoulArt.Oauth_youtube;
//
//import java.util.Map;
//
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.skybory.seoulArt.domain.user.UserRepository;
//import com.skybory.seoulArt.domain.user.entity.CustomOauth2User;
//import com.skybory.seoulArt.domain.user.entity.User;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
//
//	private final UserRepository userRepository;
//	
//	
//	@Override
//	public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
//		OAuth2User oAuth2User = super.loadUser(request);
//		String oauthClientName = request.getClientRegistration().getClientName();
//		
//		
////		try {
////			System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
////		} catch (Exception exception) {
////			exception.printStackTrace();
////		}
////		
//		User user = null;
//		String userId = null;
//		String email = "email@email.com";
//		
//		
//		// 회원가입 파트 !!! 중요
//		
//		
//		if(oauthClientName.equals("kakao")) {
//			userId = "kakao_ " + oAuth2User.getAttributes().get("id");
//			user = User.createKakao(userId);
//			user = new User(userId, "email@email.com","kakao");
//		}
//		
//		if(oauthClientName.equals("naver")) {
//			Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
//			userId = "naver_" + responseMap.get("id").substring(0,14);
//			email = responseMap.get("email");
//			user = new User(userId, email, "naver");
//		}
//		
//		userRepository.save(user);
//		
//		return new CustomOauth2User(userId);
//	}
//}
