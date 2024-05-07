package com.skybory.seoulArt.domain.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skybory.seoulArt.domain.event.controller.EventController;
import com.skybory.seoulArt.domain.user.dto.CreatorDetailResponse;
import com.skybory.seoulArt.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.examples.Example;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
//@CrossOrigin
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;
	
	// 창작자 소개 (전체)
	@GetMapping("/creators")	// postman 완료(0417)
	@Operation(summary = "창작자 목록 조회", description = "모든 창작자를 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<List<CreatorDetailResponse>> showCreatorList(){
		return ResponseEntity.ok(userService.showCreatorList());
	}
	
	// 창작자 소개2
	@GetMapping("/creators/{userId}")	// postman 완료(0417)
	@Operation(summary = "창작자 세부 조회", description = "해당 창작자의 세부사항을 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러" )
	public ResponseEntity<CreatorDetailResponse> showCreatorDetail(@Parameter(description = "유저 id") @PathVariable Long userId){
		return ResponseEntity.ok(userService.showCreatorDetail(userId));
	}
	
	@DeleteMapping("/{userId}")	// postman 완료 (0506)
	@Operation(summary = "유저 삭제", description = "해당 유저를 삭제합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<?> delete(@Parameter(description = "창작자 id") @PathVariable Long userId) {
		userService.delete(userId);
		return ResponseEntity.ok().build();
	}
	
}
