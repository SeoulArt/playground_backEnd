package com.skybory.seoulArt.domain.ticket.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.skybory.seoulArt.domain.event.entity.Event;
import com.skybory.seoulArt.domain.event.repository.EventRepository;
import com.skybory.seoulArt.domain.reply.service.ReplyServiceImpl;
import com.skybory.seoulArt.domain.seat.entity.Seat;
import com.skybory.seoulArt.domain.seat.repository.SeatRepository;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketRequest;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketResponse;
import com.skybory.seoulArt.domain.ticket.dto.TicketDetailResponse;
import com.skybory.seoulArt.domain.ticket.entity.Ticket;
import com.skybory.seoulArt.domain.ticket.repository.TicketRepository;
import com.skybory.seoulArt.domain.user.dto.UserDTO;
import com.skybory.seoulArt.domain.user.entity.User;
import com.skybory.seoulArt.domain.user.repository.UserRepository;
import com.skybory.seoulArt.global.SeatStatus;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class TicketServiceImpl implements TicketService {

	private final TicketRepository ticketRepository;
	private final UserRepository userRepository;
	private final EventRepository eventRepository;
	private final SeatRepository seatRepository;



//	@Override	// 0508 주석처리
//	@Transactional 
//	public CreateTicketResponse create(CreateTicketRequest request) {
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
//	    Long userId = userDTO.getUserId();  // UserDTO에 userId가 있다고 가정
//		// request validation check
//		User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
//		eventRepository.findById(request.getEventIdx()).orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
//		
//		// 같은 이벤트에 대해 예약하는 경우
//		if (user.getTicket().getEvent().getEventIdx() == request.getEventIdx() ) {
//			throw new ServiceException(ErrorCode.DUPLICATE_TICKET);
//		}
//		
//		// 빈 자리 확인(boolean), 빈자리 없을시 오류 던짐
//		hasAvailableSeats(request.getEventIdx());
//		
//		
//		
//		Long seatIdx = 0L;
//		// eventIdx == request.getEventIdx() 인, 자리 중에 빈 자리 하나(seatIdx가 낮은 순서부터) 예약중으로 바꿔야함.
//		if (request.getEventIdx()==1L) {
//			for(Long i = 1L; i<50; i++) {
//			Seat seat =	seatRepository.findById(i).orElseThrow(() -> new ServiceException(ErrorCode.SEAT_NOT_FOUND));
//	       
//			if (seat.getSeatStatus() == SeatStatus.AVAILABLE) {
//	            seat.setSeatStatus(SeatStatus.RESERVING);
//	            seatIdx = seat.getSeatIdx();
//	            break; // 예약된 좌석을 찾으면 루프 종료
//				}
//			}
//		}
//		
//		if (request.getEventIdx()==2L) {
//			
//		}
//		
//		if (request.getEventIdx()==3L) {
//			
//		}
//		
//		// 티켓 생성
//		Ticket ticket = new Ticket();
//		// 매핑 request -> ticket
//		mapping(request, ticket,seatIdx);
//		// DB저장
//		ticketRepository.save(ticket);
//		// DTO 반환
//		CreateTicketResponse response = new CreateTicketResponse();
//		// 매핑 request -> response
//		response.setEventIdx(request.getEventIdx());
//		response.setSeatIdx(seatIdx);
//		response.setUserIdx(request.getUserIdx());
//		return response;
//	}
	@Transactional
	@Override
	public CreateTicketResponse create(CreateTicketRequest request) {
	    log.info("create 메서드 실행");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
	    Long userId = userDTO.getUserId();  // UserDTO에 userId가 있다고 가정	    
//	    User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
	    Long eventId = request.getEventIdx();
		Event event = eventRepository.findById(eventId).orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
		
		// 유저가 티켓을 가지고있는지 판단
		log.info("canBook 실행");
		canBook(userId, event.getTitle());
		

		
		log.info("hasAvailableSeats 실행");
	    hasAvailableSeats(eventId);

	    // 좌석 번호 지정
	    Long seatIdx = findAvailableSeat(eventId);

	    // 티켓 생성
	    log.info("new Ticket() 실행");
	    Ticket ticket = new Ticket();
	    mapping(userId, ticket, seatIdx, eventId);
	    ticketRepository.save(ticket);

	    CreateTicketResponse response = new CreateTicketResponse();
	    response.setEventIdx(eventId);
	    response.setSeatIdx(seatIdx);
	    response.setUserIdx(userId);

	    return response;
	}

	
    public boolean canBook(Long userId, String newEventTitle) throws ServiceException {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        if (user.getTickets() == null || user.getTickets().isEmpty()) {
            return true;  // 티켓을 가지고 있지 않으면 예매 가능
        }
        
        // 이미 가지고 있는 티켓 중에서 새 이벤트 제목과 동일한 제목을 가진 티켓이 있는지 확인
        boolean hasDuplicateEventTicket = user.getTickets().stream()
            .anyMatch(ticket -> ticket.getEvent().getTitle().equals(newEventTitle));

        if (hasDuplicateEventTicket) {
            throw new ServiceException(ErrorCode.DUPLICATE_TICKET);
        }

        return true;  // 동일한 제목의 티켓이 없으면 예매 가능
    }

	@Transactional
	public void mapping(Long userId, Ticket ticket, Long seatIdx, Long eventId) {
		// 사용자, 이벤트 및 좌석 정보 조회
		User user = userRepository.findById(userId) .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		Event event = eventRepository.findById(eventId) .orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
		Seat seat = seatRepository.findById(seatIdx) .orElseThrow(() -> new ServiceException(ErrorCode.SEAT_NOT_FOUND));
		
		ticket.setSeat(seat);
		ticket.setUser(user);
		ticket.changeEvent(event);
		ticket.changeUser(user);
//		user.setTickets.add(ticket);
	}
	
	
	public Long findAvailableSeat(Long eventIdx) {
	    Long startSeatIdx = (eventIdx - 1) * 50 + 1; // 각 이벤트에 50개의 좌석을 가정
	    Long endSeatIdx = eventIdx * 50;
	    for (Long i = startSeatIdx; i <= endSeatIdx; i++) {
	        Seat seat = seatRepository.findById(i).orElseThrow(() -> new ServiceException(ErrorCode.SEAT_NOT_FOUND));
	        if (seat.getSeatStatus() == SeatStatus.AVAILABLE) {
	            seat.setSeatStatus(SeatStatus.RESERVING);
	            return seat.getSeatIdx(); // 예약된 좌석 인덱스 반환
	        }
	    }
	    throw new ServiceException(ErrorCode.SEAT_UNAVAILABLE); // 빈 좌석이 없을 경우 예외 발생
	}
 
	
	
	
	
//	// 공연 취소하기
//	@Override
//	@Transactional
//	public void deleteTicket(Long userId, Long ticketIdx) {
//		// 해당 사용자의 특정 이벤트에 대한 티켓을 찾아서 삭제
//		User user = userRepository.findById(userId).orElseThrow();
//		Long ticketIdx = user.getTickets().getTicketIdx();
//
//		
//        boolean hasTicket = user.getTickets().stream()
//                .anyMatch(ticket -> ticket.getTicketIdx().equals(ticketIdx));
//		
//		user.setTicket(null);
//		userRepository.save(user); // 변경된 상태를 데이터베이스에 반영
//
//		ticketRepository.findById(ticketIdx).get().getSeat().setSeatStatus(SeatStatus.AVAILABLE);
//		ticketRepository.deleteById(ticketIdx);
//	}
//

	
//	private void existTicket(final long ticketId) {
//		if(ticketRepository.existsById(ticketId)) {
//			throw new ServiceException(ErrorCode.DUPLICATE_TICKET);
//		}
//	}


	
	// 빈 자리 확인
	public void hasAvailableSeats(Long eventId) {
//		Event event = eventRepository.findById(eventIdx) .orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
		// 총 좌석 수
		int totalSeats = 50;
		int bookedSeats = (int) ticketRepository.countByEventEventIdx(eventId);
		System.out.println("bookedSeats : " + bookedSeats);
		if ( totalSeats <= bookedSeats) {
			throw new ServiceException(ErrorCode.SEAT_UNAVAILABLE);
		}
	}


	@Override
	public void deleteTicket(long userId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public TicketDetailResponse findTicket(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

// 잠시주석 0514
//	@Override
//	public TicketDetailResponse findTicket(long userId) {
//		User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
//		Ticket ticket = user.getTickets();
//		Event event = ticket.getEvent();
//		Seat seat = ticket.getSeat();
//		
//		TicketDetailResponse response = new TicketDetailResponse();
//		// dto 매핑
//		response.setEventDetail(event.getDetail());
//		response.setEventIdx(event.getEventIdx());
//		response.setEventImage(event.getImage());
//		response.setEventTitle(event.getTitle());
//		
//		response.setSeatIdx(seat.getSeatIdx());
//		response.setSeatStatus(seat.getSeatStatus());
//		
//		response.setUserId(userId);
//		response.setUsername(user.getUsername());
//		
//		response.setTicketIdx(ticket.getTicketIdx());
//		
//		return response;
//	}

//	@Override
//	public CreateTicketResponse createTicket(CreateTicketRequest request) {
//		// 사용자, 이벤트 및 좌석 정보 조회
//		User user = userRepository.findById(request.getUserIdx()) .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
//		Event event = eventRepository.findById(request.getEventIdx()) .orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
//		Seat seat = seatRepository.findById(request.getSeatIdx()) .orElseThrow(() -> new ServiceException(ErrorCode.SEAT_NOT_FOUND));
//
//		// 티켓 생성 로직
//		Ticket ticket = new Ticket();
//		ticket.setUser(user);
//		ticket.changeEvent(event);
//		ticket.setSeat(seat);
//		ticket.getSeat().setSeatStatus(SeatStatus.RESERVED);
//		// 티켓 저장 및 반환
//		return ticketRepository.save(ticket);
//	}
//	// 공연 예약하기
//	@Override
//	public Ticket createTicket(Long userIdx, Long eventIdx, Long seatIdx) {
//
//		// 사용자, 이벤트 및 좌석 정보 조회
//		User user = userRepository.findById(userIdx) .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
//		Event event = eventRepository.findById(eventIdx) .orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
//		Seat seat = seatRepository.findById(seatIdx) .orElseThrow(() -> new ServiceException(ErrorCode.SEAT_NOT_FOUND));
//
//		// 티켓 생성 로직
//		Ticket ticket = new Ticket();
//		ticket.setUser(user);
//		ticket.changeEvent(event);
//		ticket.setSeat(seat);
//		ticket.getSeat().setSeatStatus(SeatStatus.RESERVED);
//		// 티켓 저장 및 반환
//		return ticketRepository.save(ticket);
//	}
	
//	@Override
//	public boolean checkAvailability(Event event) {
//		// 이벤트의 총 좌석 수
//		int totalSeats = ticketRepository.getTotalSeatsByEvent(event);
//		// 이벤트의 예약된 좌석 수
//		int reservedSeats = ticketRepository.getReservedSeatsByEvent(event);
//		// 잔여 좌석 수 계산 ( 전체 좌석 - 예약된 좌석 )
//		int availableSeats = totalSeats - reservedSeats;
//		// 잔여 좌석이 있으면 true 반환, 없으면 false 반환
//		return availableSeats > 0;
//	}

}