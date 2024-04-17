package com.skybory.seoulArt.domain.event.service;

import java.util.List;

import java.util.Optional;
import com.skybory.seoulArt.domain.event.dto.CreateEventRequest;
import com.skybory.seoulArt.domain.event.dto.CreateEventResponse;
import com.skybory.seoulArt.domain.event.entity.Event;
import com.skybory.seoulArt.domain.event.dto.EventDetailResponse;


public interface EventService {

	// 이벤트 생성
	CreateEventResponse createEvent(CreateEventRequest request);
	
	// 모든 이벤트 조회
	List<Event> getAllEvents();
	 
	// 이벤트 ID로 이벤트 조회
	Event getEventById(Long eventIdx);
	
	// 이벤트 삭제
	boolean deleteEventById(Long eventIdx);

	EventDetailResponse showDetail(long eventId);
}
