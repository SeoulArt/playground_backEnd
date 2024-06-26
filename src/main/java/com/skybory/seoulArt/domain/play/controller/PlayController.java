package com.skybory.seoulArt.domain.play.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.skybory.seoulArt.domain.play.dto.CreatePlayRequest;
import com.skybory.seoulArt.domain.play.dto.CreatePlayResponse;
import com.skybory.seoulArt.domain.play.dto.PlayDetailResponse;
import com.skybory.seoulArt.domain.play.dto.EditPlayRequest;
import com.skybory.seoulArt.domain.play.service.PlayService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
//@CrossOrigin(origins = "http://reactserver")
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/play")	// 나중에 /thymeleaf 부분만 제거해야함.
public class PlayController {

	private final PlayService playService;
	
	// 이벤트 생성 페이지 (관리자 권한) 김태연만 사용
	@PostMapping("")		// postman 테스트 성공 0417
	@Secured("ROLE_ADMIN")
	@Operation(summary = "작품 등록", description = "작품을 등록합니다. 관리자 권한이 필요합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<CreatePlayResponse> createPlay(@RequestBody CreatePlayRequest request){
		log.info("Creating new play with request: {}", request); 
//		CreateplayResponse response = playService.createplay(request);
//		log.info("play created successfully with response: {}", response);
		return ResponseEntity.ok(playService.createPlay(request));
	}
	
	// 작품 소개
//	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
	@GetMapping("/{playId}")	// postman 테스트 성공 0417			-> put 도 만들어보고
	@Operation(summary = "작품 상세정보", description = "해당 작품 정보를 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<PlayDetailResponse> showDetail(@Parameter(description = "작품 id") @PathVariable Long playId){
		return ResponseEntity.ok(playService.showDetail(playId));
	}
	
//	@Secured("ROLE_ADMIN")		
//	@DeleteMapping("/{playId}")	// postman 테스트 성공 0418		-> 필요없음
	@Operation(summary = "작품 삭제", description = "해당 작품을 삭제합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<Boolean> deletePlay(@Parameter(description = "작품 id") @PathVariable Long playId){
		return ResponseEntity.ok(playService.deletePlayById(playId));
	}
	
	// 작품 수정
	@PutMapping("/{playId}")	// postman 테스트 성공 0417			-> put 도 만들어보고
	@Secured("ROLE_ADMIN")
	@Operation(summary = "작품 수정", description = "해당 작품 정보를 수정합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<PlayDetailResponse> editPlay(@Parameter(description = "작품 id") @PathVariable Long playId, @RequestBody EditPlayRequest request){
		return ResponseEntity.ok(playService.editPlay(playId, request));
	}
	
	
}
