package com.skybory.seoulArt.domain.event.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.ui.Model;
//import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.skybory.seoulArt.domain.event.dto.CreateEventRequest;
import com.skybory.seoulArt.domain.event.entity.Event;
import com.skybory.seoulArt.domain.event.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.skybory.seoulArt.domain.event.dto.CreateEventResponse;
import com.skybory.seoulArt.domain.event.dto.EventDetailResponse;

import lombok.RequiredArgsConstructor;

@RestController
//@CrossOrigin(origins = "http://reactserver")
@RequiredArgsConstructor
@RequestMapping("/api/event")	// 나중에 /thymeleaf 부분만 제거해야함.
public class EventController {

	private final EventService eventService;
	
	// 이벤트 생성 페이지 (관리자 권한)
	@PostMapping("/new")		// postman 테스트 성공 0417
	@Operation(summary = "작품 등록", description = "작품을 등록합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
//	@Secured("ROLE_ADMIN") // 또는  @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<CreateEventResponse> createEvent(@RequestBody CreateEventRequest request){
		return ResponseEntity.ok(eventService.createEvent(request));
	}
	
	
	// 작품 소개
	@GetMapping("/{eventId}")	// postman 테스트 성공 0417
	@Operation(summary = "작품 상세정보", description = "해당 작품 정보를 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<EventDetailResponse> showDetail(@Parameter(description = "작품 id") @PathVariable Long eventId){
		return ResponseEntity.ok(eventService.showDetail(eventId));
	}
	
//	@Secured("ROLE_ADMIN")
	@DeleteMapping("/{eventId}")	// postman 테스트 성공 0418
	@Operation(summary = "작품 삭제", description = "해당 작품을 삭제합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<Boolean> deleteEvent(@Parameter(description = "작품 id") @PathVariable Long eventId){
		return ResponseEntity.ok(eventService.deleteEventById(eventId));
	}
	
}
