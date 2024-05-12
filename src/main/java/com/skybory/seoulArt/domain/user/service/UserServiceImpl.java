package com.skybory.seoulArt.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skybory.seoulArt.Oauth.JwtUtil;
import com.skybory.seoulArt.Oauth.dto.KakaoMemberResponse;
import com.skybory.seoulArt.Oauth.dto.NaverMemberResponse;
import com.skybory.seoulArt.domain.user.dto.CreatorDetailResponse;
import com.skybory.seoulArt.domain.user.dto.UserDTO;
import com.skybory.seoulArt.domain.user.entity.User;
import com.skybory.seoulArt.domain.user.repository.UserRepository;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService{

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
	private final UserRepository userRepository;
	
    public UserDTO getCurrentUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("헤더에 JWT가 없습니다!!!");
        }

        String token = authorizationHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalStateException("JWT가 유효하지 않습니다!");
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        return loadUserByUserId(userId);
    }
	
	@Override
	public CreatorDetailResponse showCreatorDetail(long userId) {

		User user = userRepository.findById(userId).orElseThrow(()->new ServiceException(ErrorCode.USER_NOT_FOUND));		
		
		CreatorDetailResponse response = new CreatorDetailResponse();
		response.setProfileImage(user.getProfileImage());
		response.setDepartment(user.getDepartment());
		response.setDescription(user.getCreator_description());
		response.setImage(user.getCreator_image());
		response.setUsername(user.getUsername());
		
		return response;
	}
	
	@Override
	public List<CreatorDetailResponse> showCreatorList() {
	    List<User> userList = userRepository.findAll(); 
	    
	    // 옮겨담기
	    List<CreatorDetailResponse> responseList = userList.stream()
	    		// 부서가 없으면 검색되지 않음.
//	    		.filter(creator -> creator.getDepartment() != null)
	    		// ROLE_CREATOR 가 아니면 검색되지 않음
	    		.filter(creator -> creator.getRole().equals("ROLE_CREATOR"))
	    		
	    		.map(creator -> {
	    			CreatorDetailResponse response = new CreatorDetailResponse();

	    			// 매핑
	    			response.setProfileImage(creator.getProfileImage());
	    			response.setDepartment(creator.getDepartment());
	    			response.setDescription(creator.getCreator_description());
	    			response.setImage(creator.getCreator_image());
	    			response.setUsername(creator.getUsername());
	    			return response;
	    		})
	    		.collect(Collectors.toList());
	    return responseList;
	}
	
	@Override
	@Transactional
	public void delete(Long userId) {
		userRepository.deleteById(userId);
	}

	
	@Override
	@Transactional
	public UserDTO register(KakaoMemberResponse kakaoMemberResponse) {
		// 회원 가입하자
		User user = new User();
		
		kakaoMemberResponse.getProperties().getNickname();	// '김태연' 가져옴
		user.setProfileImage(kakaoMemberResponse.getProperties().getProfile_image());	// 프사 : 링크 걸어줌
		user.setRole("ROLE_USER");	//왜인지 모르지만 롤이 설정되고있음..
		user.setUsername(kakaoMemberResponse.getProperties().getNickname());	//테스트삼아 해보자 뭐가 후순위인지 궁금하네
		user.setVerifyCode("kakao " + kakaoMemberResponse.getId());
		userRepository.save(user);
		
		UserDTO response = new UserDTO();
		response.setProfileImage(user.getProfileImage());
		response.setRole(user.getRole());
		response.setUsername(user.getUsername());
		response.setUserId(user.getId());
		return response;
	}

	@Override
	@Transactional
	public UserDTO register(NaverMemberResponse naverMemberResponse) {
		// 회원 가입하자
		System.out.println("네이버 회원가입 시작");
		User user = new User();
		
		user.setProfileImage(naverMemberResponse.getNaverUserDetail().getProfile_image());	// 프사 : abc.jpg
		user.setRole("ROLE_USER");												// ROLE_USER
		user.setUsername(naverMemberResponse.getNaverUserDetail().getName());	 // username : 김태연
		user.setVerifyCode("naver " + naverMemberResponse.getNaverUserDetail().getId());	// verifycode : naver soaijogjfsi
		
		userRepository.save(user);
		
		UserDTO response = new UserDTO();
		response.setProfileImage(user.getProfileImage());	// profileImage : abc.jpg
		response.setRole(user.getRole());			// role : ROLE_USER
		response.setUsername(user.getUsername());	// username : 김태연
		response.setUserId(user.getId());			// id : 1
		return response;
	}

//	@Override
//	@Transactional
//	public UserDTO naverRegister(NaverMemberResponse naverMemberResponse) {
//		// 회원 가입하자
//		User user = new User();
//		
//		kakaoMemberResponse.getProperties().getNickname();	// '김태연' 가져옴
//		user.setProfileImage(kakaoMemberResponse.getProperties().getProfile_image());	// 프사 : 링크 걸어줌
//		user.setRole("ROLE_USER");	//왜인지 모르지만 롤이 설정되고있음..
//		user.setUsername(kakaoMemberResponse.getProperties().getNickname());	//테스트삼아 해보자 뭐가 후순위인지 궁금하네
//		user.setVerifyCode("kakao " + kakaoMemberResponse.getId());
//		userRepository.save(user);
//		
//		UserDTO response = new UserDTO();
//		response.setProfileImage(user.getProfileImage());
//		response.setRole(user.getRole());
//		response.setUsername(user.getUsername());
//		response.setUserId(user.getId());
//		return response;
//	}

	@Override
	@Transactional
	public UserDTO login(KakaoMemberResponse kakaoMemberResponse) {
		UserDTO response = new UserDTO();
		
		String verifyCode = "kakao " + kakaoMemberResponse.getId();	//	3432634754 -> 이 값을 반드시 저장해야함.
		User user = userRepository.findByVerifyCode(verifyCode);
		// 아이디 검색을 했는데 있을 경우..
		if(userRepository.findByVerifyCode(verifyCode) != null ) {
			response.setProfileImage(user.getProfileImage());
			response.setRole(user.getRole());
			response.setUserId(user.getId());
			response.setUsername(user.getUsername());
			System.out.println("카카오 로그인 : 나 아이디 검색했는데 있더라");
			// 그녀석 그대로 가져와.
		} else {
		// 아이디 검색을 했는데 없을 경우 -> 회원가입
		response = register(kakaoMemberResponse);
 
		}
		return response;
	}

	@Override
	@Transactional
	public UserDTO login(NaverMemberResponse naverMemberResponse) {
		UserDTO response = new UserDTO();
		
		String verifyCode = "naver " + naverMemberResponse.getNaverUserDetail().getId();	//	3432634754 -> 이 값을 반드시 저장해야함.
		User user = userRepository.findByVerifyCode(verifyCode);
		// 아이디 검색을 했는데 있을 경우..
		if(userRepository.findByVerifyCode(verifyCode) != null ) {
			response.setProfileImage(user.getProfileImage());
			response.setRole(user.getRole());
			response.setUserId(user.getId());
			response.setUsername(user.getUsername());
			System.out.println("네이버 로그인 : 나 아이디 검색했는데 있더라");
			// 그녀석 그대로 가져와.
		} else {
		// 아이디 검색을 했는데 없을 경우 -> 회원가입
		response = register(naverMemberResponse);
 
		}
		return response;	}

//	@Override
//	@Transactional
//	public UserDTO naverLogin(NaverMemberResponse naverMemberResponse) {
//		UserDTO response = new UserDTO();
//		
//		String verifyCode = "naver " + naverMemberResponse.getId();	//	3432634754 -> 이 값을 반드시 저장해야함.
//		User user = userRepository.findByVerifyCode(verifyCode);
//		// 아이디 검색을 했는데 있을 경우..
//		if(userRepository.findByVerifyCode(verifyCode) != null ) {
//			response.setProfileImage(user.getProfileImage());
//			response.setRole(user.getRole());
//			response.setUserId(user.getId());
//			response.setUsername(user.getUsername());
//			System.out.println("나 아이디 검색했는데 있더라");
//			// 그녀석 그대로 가져와.
//		} else {
//			// 아이디 검색을 했는데 없을 경우 -> 회원가입
//			response = register(naverMemberResponse);
//			
//		}
//		return response;
//	}
//	    
    @Override
    public UserDTO loadUserByUserId(Long userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()->new ServiceException(ErrorCode.USER_NOT_FOUND));	

        
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole());
        userDTO.setProfileImage(user.getProfileImage());
        log.info("UserDTO returned from loadUserByUserId: " + userDTO);
        return userDTO;
    }
	    
	
}
