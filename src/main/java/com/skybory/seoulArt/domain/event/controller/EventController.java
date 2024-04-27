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


import com.skybory.seoulArt.domain.event.dto.CreateEventResponse;
import com.skybory.seoulArt.domain.event.dto.EventDetailResponse;

import lombok.RequiredArgsConstructor;

@RestController
//@CrossOrigin(origins = "http://reactserver")
@RequiredArgsConstructor
@RequestMapping("/api/event")	// 나중에 /thymeleaf 부분만 제거해야함.
public class EventController {

	private final EventService eventService;
	
	
	// 작품 소개
	@GetMapping("/{eventId}")	// postman 테스트 성공 0417
	public ResponseEntity<EventDetailResponse> showDetail(@PathVariable long eventId){
		return ResponseEntity.ok(eventService.showDetail(eventId));
	}
	
	// 이벤트 생성 페이지 (관리자 권한)
	@PostMapping("/create")		// postman 테스트 성공 0417
//	@Secured("ROLE_ADMIN") // 또는  @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<CreateEventResponse> createEvent(@RequestBody CreateEventRequest request){
		return ResponseEntity.ok(eventService.createEvent(request));
	}
	
	@DeleteMapping("/delete/{eventId}")	// postman 테스트 성공 0418
//	@Secured("ROLE_ADMIN")
	public ResponseEntity<Boolean> deleteEvent(@PathVariable long eventId){
		return ResponseEntity.ok(eventService.deleteEventById(eventId));
	}
	
}
