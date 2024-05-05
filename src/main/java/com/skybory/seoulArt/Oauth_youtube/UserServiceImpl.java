//package com.skybory.seoulArt.Oauth_youtube;
//
//import org.springframework.stereotype.Service;
//import com.skybory.ticketing.dto.JoinDTO;
//import com.skybory.ticketing.dto.LoginFormDTO;
//import com.skybory.ticketing.entity.User;
//import com.skybory.ticketing.repository.UserRepository;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//
//	private final UserRepository userRepository;
//
//	@Override // 회원가입
//	@Transactional
//	public User createUser(JoinDTO joinDTO) {
//		
//		// 1. 회원가입DTO 유효성 검사
//		validateJoinDTO(joinDTO);
//
//		// 2. DTO 와 엔티티 매핑(값 저장하기)
//	    User user = mapJoinDTOToUser(joinDTO);
//	    
//		// 3. 사용자 저장 및 반환
//		return userRepository.save(user);
//	}
//
//	@Override // 로그인
//	public User login(LoginFormDTO loginForm) {
//		
//		// 전화번호와 인증 코드 가져오기
//		String userPhoneNumber = loginForm.getUserPhoneNumber();
//		String userVerifyCode = loginForm.getUserVerifyCode();
//
//		User user = userRepository.findByUserPhoneNumberAndUserVerifyCode(userPhoneNumber, userVerifyCode);
//		// 로그인이 실패했을 경우(아이디 비밀번호가 안맞는 경우
//		if (user != null) {
//			System.out.println("회원을 찾을 수 없습니다.");
//			return user;
//		} else {
//			// 전화번호를 사용하여 사용자 조회
//			return user;
//		}
//
//	}
//
//	@Override
//	public void changeVerifyCode(Long userIdx, String newVerifyCode) {
//		System.out.println("kgasoijiagjiogjoiaego");
//		User user = userRepository.findById(userIdx).orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
//		user.setUserVerifyCode(newVerifyCode);
//	}
//	
//	
//	private void validateJoinDTO(JoinDTO joinDTO) {
//		// 필요한 유효성 검사 수행
//		if (joinDTO == null || joinDTO.getUserName() == null || joinDTO.getUserPhoneNumber() == null) {
//			throw new IllegalArgumentException("회원가입 정보가 올바르지 않습니다.");
//		}
//		// UserVerifyCode 값이 정수가 아닌 경우 확인
//		if (!joinDTO.getUserVerifyCode().matches("^\\d{4}$")) {
//			throw new IllegalArgumentException("유저 인증 코드는 4자리 숫자여야 합니다.");
//		}
//	}
//
//	private User mapJoinDTOToUser(JoinDTO joinDTO) {
//	    User user = new User();
//	    user.setUserName(joinDTO.getUserName());
//	    user.setUserPhoneNumber(joinDTO.getUserPhoneNumber());
//	    user.setUserVerifyCode(joinDTO.getUserVerifyCode());
//	    return user;
//	}
//
//	@Override
//	public void logout() {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	
//
//}