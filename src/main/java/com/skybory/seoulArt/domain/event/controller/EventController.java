package com.skybory.seoulArt.domain.event.controller;

import org.springframework.http.ResponseEntity;


import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.skybory.seoulArt.domain.event.dto.EventDetailRequest;
import com.skybory.seoulArt.domain.event.dto.CreateEventRequest;
import com.skybory.seoulArt.domain.event.entity.Event;
import com.skybory.seoulArt.domain.event.service.EventService;
import com.skybory.seoulArt.domain.event.dto.CreateEventResponse;
import com.skybory.seoulArt.domain.event.dto.EventDescriptionResponse;

import lombok.RequiredArgsConstructor;

@RestController
//@CrossOrigin(origins = "http://reactserver")
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

	private final EventService eventService;
	
	
	// 이벤트 조회 페이지(메인 이벤트)
	// figma 작품소개 메인
	@GetMapping("/main")
	public ResponseEntity<Event> eventMain(@RequestBody Long eventIdx){
		Event event = eventService.getEventById(eventIdx);
		return ResponseEntity.ok(event);
	}
	
	// 이벤트 생성 페이지 (관리자 권한)
	@PostMapping("/create")
	@Secured("ROLE_ADMIN") // 또는  @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<CreateEventResponse> createEvent(@RequestBody CreateEventRequest request){
		return ResponseEntity.ok(eventService.createEvent(request));
	}
	
	// ==========================================4월13일. FIGMA 매칭 시작 + 리팩토링
	// 작품 소개
	@GetMapping("/detail")
	public ResponseEntity<EventDescriptionResponse> showDetail(@RequestBody EventDetailRequest request){
		return ResponseEntity.ok(eventService.showDetail(request));
	}
	
//	// 창작자 소개 (전체)
//	@GetMapping("/creators")
//	public ResponseEntity<EventCreatorListResponse> showCreatorList(@RequestBody long eventIdx){
//		return ResponseEntity.ok(eventService.showCreatorList(eventIdx));
//	}
//	
//	// 창작자 소개2
//	@GetMapping("/creators/{creatorId}")
//	public ResponseEntity<EventCreatorDetailResponse> showCreatorDetail(@PathVariable long creatorIdx){
//		return ResponseEntity.ok(eventService.showCreatorDetail(creatorIdx));
//	}
//	
	@DeleteMapping("/delete/{eventId}")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<Boolean> deleteEvent(@PathVariable long eventId){
		return ResponseEntity.ok(eventService.deleteEventById(eventId));
	}
	
	
//	@GetMapping("/creator/{creatorId}")
//	public ResponseEntity<CreatorDetail>
//	// Q&A 는 뭔지 모르겠음.
//	
//	@GetMapping("/Q&A")
//	public ResponseEntity<T>
//	
	
	
}
