package com.skybory.seoulArt.domain.user.service;

import java.util.List;

import com.skybory.seoulArt.domain.user.dto.CreatorDetailResponse;

public interface UserService {

	CreatorDetailResponse showCreatorDetail(long userId);

	List<CreatorDetailResponse> showCreatorList();
	
}
