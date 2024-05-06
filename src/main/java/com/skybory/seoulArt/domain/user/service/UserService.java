package com.skybory.seoulArt.domain.user.service;

import java.util.List;

import com.skybory.seoulArt.OauthWithReact.KakaoMemberResponse;
import com.skybory.seoulArt.OauthWithReact.NaverMemberResponse;
import com.skybory.seoulArt.domain.user.dto.CreatorDetailResponse;
import com.skybory.seoulArt.domain.user.dto.UserDTO;
import com.skybory.seoulArt.domain.user.entity.User;

public interface UserService {

	CreatorDetailResponse showCreatorDetail(long userId);

	List<CreatorDetailResponse> showCreatorList();
	
	void delete(Long userId);
	

	UserDTO register(KakaoMemberResponse kakaoMemberResponse);

	UserDTO login(KakaoMemberResponse kakaoMemberResponse);

	UserDTO login(NaverMemberResponse naverMemberResponse);

	UserDTO register(NaverMemberResponse naverMemberResponse);
	
	
}
