package com.skybory.seoulArt.domain.user.service;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.skybory.seoulArt.Oauth.dto.KakaoMemberResponse;
import com.skybory.seoulArt.Oauth.dto.NaverMemberResponse;
import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceRequest;
import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceResponse;
import com.skybory.seoulArt.domain.user.dto.CreatorDetailResponse;
import com.skybory.seoulArt.domain.user.dto.CreatorListResponse;
import com.skybory.seoulArt.domain.user.dto.ImageRequest;
import com.skybory.seoulArt.domain.user.dto.ImageResponse;
import com.skybory.seoulArt.domain.user.dto.UserDTO;
import com.skybory.seoulArt.domain.user.dto.UserMobileRequest;
import com.skybory.seoulArt.domain.user.dto.UserMobileResponse;
import com.skybory.seoulArt.domain.user.entity.User;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

	CreatorDetailResponse showCreatorDetail(Long userId);

	List<CreatorListResponse> showCreatorList();
	
	void delete(Long userId);
	

	UserDTO register(KakaoMemberResponse kakaoMemberResponse);

	UserDTO login(KakaoMemberResponse kakaoMemberResponse);

	UserDTO login(NaverMemberResponse naverMemberResponse);

	UserDTO register(NaverMemberResponse naverMemberResponse);

	UserDTO loadUserByUserId(Long userId) throws UsernameNotFoundException;

//	UserDTO getCurrentUser(HttpServletRequest request);

//	CreatorDetailResponse postIntroduction(CreatorDetailRequest request) throws IOException;

//	UserMobileResponse setMobile(UserMobileRequest request);
	
//	User findUserByCookie();

	User findUserByHeader(HttpServletRequest request);

	UserMobileResponse setMobile(UserMobileRequest request, HttpServletRequest requestServlet);

	CreatorIntroduceResponse postIntroduction(CreatorIntroduceRequest request, HttpServletRequest requestServlet)
			throws IOException;

	ImageResponse putProfileImage(ImageRequest request, HttpServletRequest requestServlet) throws IOException;
	
}
