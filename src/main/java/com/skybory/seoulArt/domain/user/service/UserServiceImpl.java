package com.skybory.seoulArt.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skybory.seoulArt.domain.user.UserRepository;
import com.skybory.seoulArt.domain.user.dto.CreatorDetailResponse;
import com.skybory.seoulArt.domain.user.entity.User;
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
		response.setProfileImage(user.getProfileImage());
		response.setDepartment(user.getDepartment());
		response.setDescription(user.getDescription());
		response.setImage(user.getImage());
		response.setUsername(user.getUsername());
		
		return response;
	}
	
	@Override
	public List<CreatorDetailResponse> showCreatorList() {
	    List<User> userList = userRepository.findAll(); 
	    
	    // 옮겨담기
	    List<CreatorDetailResponse> responseList = userList.stream()
	    		.filter(creator -> creator.getDepartment() != null)
	    		.map(creator -> {
	    			CreatorDetailResponse response = new CreatorDetailResponse();

	    			// 매핑
	    			response.setProfileImage(creator.getProfileImage());
	    			response.setDepartment(creator.getDepartment());
	    			response.setDescription(creator.getDescription());
	    			response.setImage(creator.getImage());
	    			response.setUsername(creator.getUsername());
	    			return response;
	    		})
	    		.collect(Collectors.toList());
	    return responseList;
	}
	    
	    
//	    return userList.stream()
//	                   .filter(user -> user.getDepartment() != null)
//	                   .collect(Collectors.toList());

}
