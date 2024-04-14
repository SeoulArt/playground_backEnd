package com.skybory.seoulArt.domain.event.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.skybory.seoulArt.domain.event.dto.EventDetailRequest;
import com.skybory.seoulArt.domain.event.dto.RegisterEventDTO;
import com.skybory.seoulArt.domain.event.entity.Creator;
import com.skybory.seoulArt.domain.event.entity.Event;
import com.skybory.seoulArt.domain.event.repository.CreatorRepository;
import com.skybory.seoulArt.domain.event.repository.EventRepository;
import com.skybory.seoulArt.domain.event.dto.EventCreatorDetailResponse;
import com.skybory.seoulArt.domain.event.dto.EventCreatorListResponse;
import com.skybory.seoulArt.domain.event.dto.EventDescriptionResponse;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

// 트랜잭션 리드온리 추가해야함
@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

	private final EventRepository eventRepository;
	private final CreatorRepository creatorRepository;
	
    @Override
    public Event createEvent(RegisterEventDTO registerEventDTO) {
    	
    	// 1. validation 체크 해야할까?
//    	validateEventDTO(registerEventDTO);
    	
        // 2. 새로운 이벤트 생성
        Event event = mapJoinDTOToUser(registerEventDTO);
        
        // 3. 이벤트 저장
        return eventRepository.save(event);
    }

	private Event mapJoinDTOToUser(RegisterEventDTO registerEventDTO) {
	    Event event = new Event();
	    event.setEventTitle(registerEventDTO.getEventTitle());
	    event.setEventDetail(registerEventDTO.getEventDetail());
	    event.setEventImage(registerEventDTO.getEventImage());
	    
	    return event;
	}
	
    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    @Override
    public Event getEventById(Long eventIdx) {
        return eventRepository.findById(eventIdx)
                               .orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
    }

    @Override
    public void deleteEventById(Long eventIdx) {
        eventRepository.deleteById(eventIdx);
    }

    
    
	@Override
	public EventDescriptionResponse showDetail(EventDetailRequest request) {
		// 이벤트 찾기
//		Event event = eventRepository.findById(request.getId())
//				.orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
		// 정보 가져오기 및 반환하기
		return eventRepository.findEventDeatilByEventIdx(request.getId())
				.orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
	}

	@Override
	public EventCreatorListResponse showCreatorList(long eventIdx) {
//		// 이벤트 찾기
//		Event event = eventRepository.findById(eventId).orElseThrow();
//		
//		// 크리에이터 꺼내기
//		List<Creator> creator = event.getCreator();
//		   // EventCreatorListResponse에 설정
//	    EventCreatorListResponse response = new EventCreatorListResponse(creator);
		return eventRepository.findCreatorByEventIdx(eventIdx)
				.orElseThrow(() -> new ServiceException(ErrorCode.CREATOR_NOT_FOUND));
	}

	@Override
	public EventCreatorDetailResponse showCreatorDetail(long creatorIdx) {

		// 창작자 찾기
		Creator creator =creatorRepository.findById(creatorIdx)
				.orElseThrow(() -> new ServiceException(ErrorCode.CREATOR_NOT_FOUND));
		// 값 매핑하기
		EventCreatorDetailResponse response = new EventCreatorDetailResponse();
		response.setDepartment(creator.getDepartment());
		response.setDescription(creator.getDescription());
		response.setId(creatorIdx);
		response.setImage(creator.getImage());
		response.setName(creator.getName());
		
		return response;
	}

}
