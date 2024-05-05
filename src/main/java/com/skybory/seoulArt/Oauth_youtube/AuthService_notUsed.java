//package com.skybory.seoulArt.Oauth_youtube;
//
//import java.util.Optional;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.skybory.ticketing.dto.KaKaoAccessTokenResponse;
//import com.skybory.ticketing.dto.KakaoMemberResponse;
//import com.skybory.ticketing.entity.User;
//import com.skybory.ticketing.provider.KaKaoMemberInfoRequestException;
//import com.skybory.ticketing.provider.KakaoServerException;
//import com.skybory.ticketing.provider.KakaoTokenRequestException;
//import com.skybory.ticketing.provider.OAuthProvider;
//import com.skybory.ticketing.repository.UserRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class AuthService_notUsed {
//
//    private final OAuthProvider provider;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public User login(String authorizationCode) throws KakaoServerException, KakaoTokenRequestException, KaKaoMemberInfoRequestException {
//      
//    	// 엑세서 토큰 받아오기
//    	KaKaoAccessTokenResponse accessTokenResponse = provider.getAccessToken(authorizationCode);
//        String accessToken = accessTokenResponse.getAccessToken();
//
//        // 엑세스 토큰을 이용하여 카카오 서버에서 사용자 정보 가져오기
//        KakaoMemberResponse kakaoMemberResponse = provider.getMemberInfo(accessToken);
//        
//        // 3. 가져온 사용자 정보를 바탕으로 로컬 데이터베이스에 사용자를 등록하거나 조회합니다.
//        Long kakaoUserId = kakaoMemberResponse.getId();
//        Optional<User> optionalUser = userRepository.findByKakaoUserId(kakaoUserId);
//        
//        // 4. 사용자가 이미 등록되어 있는 경우에는 해당 사용자를 가져오고, 등록되어 있지 않은 경우에는 새로운 사용자를 생성하여 저장합니다.
//        User user;
//        if (optionalUser.isPresent()) {
//            user = optionalUser.get();
//        } else {
//            user = User.builder()
//                    .kakaoUserId(kakaoUserId)
//                    .build();
//            userRepository.save(user);
//        }
//
//        // 5. 사용자 정보를 반환합니다.
//        return user;
//    }
//}