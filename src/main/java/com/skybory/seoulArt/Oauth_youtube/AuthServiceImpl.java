//package com.skybory.seoulArt.Oauth_youtube;
//
//import org.springframework.http.ResponseEntity;
//
//import com.skybory.seoulArt.domain.user.UserRepository;
//import com.skybory.seoulArt.domain.user.entity.User;
//
//public class AuthServiceImpl implements AuthService{
//
//	private UserRepository userRepository;
//	@Override
//	public ResponseEntity<? super SignInResponseDTO> signIn(SignInRequestDTO dto) {
//
//		String token = null;
//		
//		try {
//			String userId = dto.getId();
//			User user = userRepository.findByUserId(userId);
//			
//			// 유저가 아이디로 검색 안될 경우 오류 반환
//			if (user == null) SignInResponseDTO.signInFail();
//			
//			String password = dto.getPassword();
//			String encodedPassWord = user.getPassword();
//			boolean isMatched = passwordEncoder.matches(password, encodedPassword);
//			if (!isMatched) return SignInResponseDTO.signInFail();
//			
//			token = jwtProvider.create(userId);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseDTO.databaseError();
//		}
//		
//		return SignInResponseDTO.success(token);
//	}
//
//	
//	
//	
//}
