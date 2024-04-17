package com.skybory.seoulArt.domain.event.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.skybory.seoulArt.domain.event.dto.CreateEventRequest;
import com.skybory.seoulArt.domain.event.dto.CreateEventResponse;
import com.skybory.seoulArt.domain.event.entity.Event;
import com.skybory.seoulArt.domain.event.repository.EventRepository;
import com.skybory.seoulArt.domain.event.dto.EventDetailResponse;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import lombok.RequiredArgsConstructor;

// 트랜잭션 리드온리 추가해야함
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;

	@Override
	@Transactional
	public CreateEventResponse createEvent(CreateEventRequest request) {

		// 1. validation 체크 해야할까?
//    	validateEventDTO(request);

//        // 2. 새로운 이벤트 생성 및 매핑
		Event event = mapJoinDTOToUser(request);

		// 3. DB에 이벤트 저장
		eventRepository.save(event);

		// 4. response dto 생성 및 반환
		CreateEventResponse response = new CreateEventResponse();

		// 5. dto에 값 미팽
		response.setEventDetail(request.getDetail());
		response.setEventImage(request.getImage());
		response.setEventTitle(request.getTitle());

		// 6. 응답 반환
		return response;
	}

	// 매핑 메서드
	private Event mapJoinDTOToUser(CreateEventRequest registerEventDTO) {
		Event event = new Event();
		event.setTitle(registerEventDTO.getTitle());
		event.setDetail(registerEventDTO.getDetail());
		event.setImage(registerEventDTO.getImage());

		return event;
	}

	@Override
	public List<Event> getAllEvents() {
		return eventRepository.findAll();
	}

	@Override
	public Event getEventById(Long eventIdx) {
		return eventRepository.findById(eventIdx).orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
	}

	@Override
	@Transactional
	public boolean deleteEventById(Long eventIdx) {
		try {
			eventRepository.deleteById(eventIdx);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public EventDetailResponse showDetail(long eventId) {
		// 이벤트 찾기
		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
		// 정보 가져오기 및 반환하기
		EventDetailResponse response = new EventDetailResponse();
		response.setEventIdx(event.getEventIdx());
		response.setTitle(event.getTitle());
		response.setDetail(event.getDetail());
		response.setImage(event.getImage());
		return response;
	}

//	@Override
//	public EventCreatorListResponse showCreatorList(long eventIdx) {
////		// 이벤트 찾기
////		Event event = eventRepository.findById(eventId).orElseThrow();
////		
////		// 크리에이터 꺼내기
////		List<Creator> creator = event.getCreator();
////		   // EventCreatorListResponse에 설정
////	    EventCreatorListResponse response = new EventCreatorListResponse(creator);
//		return eventRepository.findCreatorByEventIdx(eventIdx)
//				.orElseThrow(() -> new ServiceException(ErrorCode.CREATOR_NOT_FOUND));
//	}

//	@Override
//	public EventCreatorListResponse showCreatorList(long eventIdx) {
//		// 이벤트 찾기
//		Event event = eventRepository.findById(eventIdx)
//				.orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
//
//		// 이벤트에서 크리에이터 목록 가져오기
//		List<Creator> creators = event.getCreator();
//
//		// EventCreatorListResponse에 설정
//		EventCreatorListResponse response = new EventCreatorListResponse(creators);
//
//		return response;
//	}
//
//	@Override
//	public CreatorDetailResponse showCreatorDetail(long creatorIdx) {
//
//		// 창작자 찾기
//		Creator creator = creatorRepository.findById(creatorIdx)
//				.orElseThrow(() -> new ServiceException(ErrorCode.CREATOR_NOT_FOUND));
//		// 값 매핑하기
//		CreatorDetailResponse response = new CreatorDetailResponse();
//		response.setDepartment(creator.getDepartment());
//		response.setDescription(creator.getDescription());
//		response.setId(creatorIdx);
//		response.setImage(creator.getImage());
//		response.setName(creator.getName());
//
//		return response;
//	}

}
