package com.skybory.seoulArt.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skybory.seoulArt.domain.user.User;
import com.skybory.seoulArt.domain.user.UserRepository;
import com.skybory.seoulArt.domain.user.dto.CreatorDetailResponse;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import lombok.RequiredArgsConstructor;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	@Override
	public CreatorDetailResponse showCreatorDetail(long userId) {

		User user = userRepository.findById(userId).orElseThrow(()->new ServiceException(ErrorCode.USER_NOT_FOUND));		
		
		CreatorDetailResponse response = new CreatorDetailResponse();
		response.setDepartment(user.getDepartment());
		response.setDescription(user.getDescription());
		response.setImage(user.getImage());
		response.setUsername(user.getUsername());
		
		return response;
	}
	@Override
	public List<CreatorDetailResponse> showCreatorList() {
		// TODO Auto-generated method stub
		return null;
	}

}
