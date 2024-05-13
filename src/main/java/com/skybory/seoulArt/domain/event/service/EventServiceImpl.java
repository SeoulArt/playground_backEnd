package com.skybory.seoulArt.domain.event.service;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.skybory.seoulArt.domain.event.controller.EventController;
import com.skybory.seoulArt.domain.event.dto.CreateEventRequest;
import com.skybory.seoulArt.domain.event.dto.CreateEventResponse;
import com.skybory.seoulArt.domain.event.entity.Event;
import com.skybory.seoulArt.domain.event.repository.EventRepository;
import com.skybory.seoulArt.domain.event.dto.EventDetailResponse;
import com.skybory.seoulArt.domain.event.dto.EventEditRequest;
import com.skybory.seoulArt.global.FileUploadService;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

// 트랜잭션 리드온리 추가해야함
@Service
@Transactional(readOnly = true)
@Log4j2
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;
	private final FileUploadService fileUploadService;  // FileUploadService 주입

//	@Override
//	@Transactional
//	public CreateEventResponse createEvent(CreateEventRequest request) {
//
//		// 1. validation 체크 해야할까?
////    	validateEventDTO(request);
//
////        // 2. 새로운 이벤트 생성 및 매핑
//		Event event = mapJoinDTOToUser(request);
//
//		// 3. DB에 이벤트 저장
//		eventRepository.save(event);
//
//		// 4. response dto 생성 및 반환
//		CreateEventResponse response = new CreateEventResponse();
//
//		// 5. dto에 값 미팽
//		response.setEventDetail(request.getDetail());
//		response.setEventImage(request.getImage());
//		response.setEventTitle(request.getTitle());
//
//		// 6. 응답 반환
//		return response;
//	}
	
	@Override
	@Transactional
	public CreateEventResponse createEvent(CreateEventRequest request) {
		try {
			validateEventDTO(request);
			Event event = mapJoinDTOToUser(request);
			eventRepository.save(event);
		}
			
			catch (DataIntegrityViolationException e) {
			    log.error("Data integrity violation on creating event: {}", e.getMessage());
			    throw new ServiceException(ErrorCode.INVALID_INPUT_VALUE);
			} catch (EntityNotFoundException e) {
			    log.error("Entity not found when creating event: {}", e.getMessage());
			    throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND);
			}
		CreateEventResponse response = new CreateEventResponse();
		response.setEventDetail(request.getDetail());
		response.setEventImage(request.getImage());
		response.setEventTitle(request.getTitle());

		return response;
	}
//	@Override
//	@Transactional
//	public CreateEventResponse createEvent(CreateEventRequest request) {
//	    try {
//	        validateEventDTO(request);
//	        MultipartFile imageFile = request.getImageFile();
//	        
//	        if (imageFile != null && !imageFile.isEmpty()) {
//	            String fileName = "events/" + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
//	            String imageUrl = fileUploadService.uploadFile(imageFile, fileName);
//	            request.setImage(imageUrl);  // 업로드된 이미지 URL을 request의 image 필드에 저장
//	        }
//
//	        Event event = mapJoinDTOToUser(request);
//	        event.setImage(request.getImage());  // Event 엔티티에 이미지 URL을 저장
//	        eventRepository.save(event);
//
//	    } catch (DataIntegrityViolationException e) {
//	        log.error("Data integrity violation on creating event: {}", e.getMessage());
//	        throw new ServiceException(ErrorCode.INVALID_INPUT_VALUE);
//	    } catch (EntityNotFoundException e) {
//	        log.error("Entity not found when creating event: {}", e.getMessage());
//	        throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND);
//	    } catch (IOException e) {
//	        log.error("Error uploading image to S3: {}", e.getMessage());
//	        throw new RuntimeException("Error uploading image", e);
//	    }
//
//	    CreateEventResponse response = new CreateEventResponse();
//	    response.setEventDetail(request.getDetail());
//	    response.setEventImage(request.getImage());  // 응답에 업로드된 이미지의 URL 포함
//	    response.setEventTitle(request.getTitle());
//
//	    return response;
//	}
	
	private void validateEventDTO(CreateEventRequest request) {
	    // 여기에 유효성 검사 로직을 구현, 실패 시 IllegalArgumentException 던지기
	    if (request.getDetail() == null || request.getImage() == null || request.getTitle() == null) {
	        throw new IllegalArgumentException("Event detail, image, and title must not be null");
	    }
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
	public EventDetailResponse showDetail(Long eventId) {
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


	@Override
	@Transactional
	public EventDetailResponse editEvent(Long eventId, EventEditRequest request) {
		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
		
		event.setTitle(request.getEventTitle());
		event.setDetail(request.getEventDetail());
		event.setImage(request.getEventImage());
		
		
		EventDetailResponse response = new EventDetailResponse();
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
