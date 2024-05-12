package com.skybory.seoulArt.domain.user.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.skybory.seoulArt.Oauth.dto.KakaoMemberResponse;
import com.skybory.seoulArt.Oauth.dto.NaverMemberResponse;
import com.skybory.seoulArt.domain.user.dto.CreatorDetailResponse;
import com.skybory.seoulArt.domain.user.dto.UserDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

	CreatorDetailResponse showCreatorDetail(long userId);

	List<CreatorDetailResponse> showCreatorList();
	
	void delete(Long userId);
	

	UserDTO register(KakaoMemberResponse kakaoMemberResponse);

	UserDTO login(KakaoMemberResponse kakaoMemberResponse);

	UserDTO login(NaverMemberResponse naverMemberResponse);

	UserDTO register(NaverMemberResponse naverMemberResponse);

	UserDTO loadUserByUserId(Long userId) throws UsernameNotFoundException;

	UserDTO getCurrentUser(HttpServletRequest request);

	
	
}
