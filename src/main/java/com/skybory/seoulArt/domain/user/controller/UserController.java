package com.skybory.seoulArt.domain.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skybory.seoulArt.domain.user.dto.CreatorDetailResponse;
import com.skybory.seoulArt.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
//@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	
	// 창작자 소개 (전체)
	@GetMapping("/creators")	// postman 완료(0417)
	public ResponseEntity<List<CreatorDetailResponse>> showCreatorList(){
		return ResponseEntity.ok(userService.showCreatorList());
	}
	
	// 창작자 소개2
	@GetMapping("/creators/{userId}")	// postman 완료(0417)
	public ResponseEntity<CreatorDetailResponse> showCreatorDetail(@PathVariable long userId){
		return ResponseEntity.ok(userService.showCreatorDetail(userId));
	}
	
}
