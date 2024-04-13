package com.skybory.seoulArt.domain.event;

import java.util.List;
import java.util.Optional;

import com.skybory.seoulArt.domain.event.dto.EventDetailRequest;
import com.skybory.seoulArt.domain.event.entity.Event;
import com.skybory.seoulArt.domain.event.dto.EventCreatorListResponse;
import com.skybory.seoulArt.domain.event.dto.EventDescriptionResponse;


public interface EventService {

	// 이벤트 생성
	Event createEvent(RegisterEventDTO registerEventDTO);
	
	// 모든 이벤트 조회
	List<Event> getAllEvents();
	 
	// 이벤트 ID로 이벤트 조회
	Event getEventById(Long eventIdx);
	
	// 이벤트 삭제
	void deleteEventById(Long eventIdx);

	EventDescriptionResponse showDetail(EventDetailRequest request);

	EventCreatorListResponse showCreatorList(long eventId);

//	EventDetailResponse showDetail(EventDetailRequest request);

}
