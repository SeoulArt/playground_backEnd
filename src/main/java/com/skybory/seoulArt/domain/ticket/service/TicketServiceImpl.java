package com.skybory.seoulArt.domain.ticket.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skybory.seoulArt.domain.event.entity.Event;
import com.skybory.seoulArt.domain.event.repository.EventRepository;
import com.skybory.seoulArt.domain.seat.entity.Seat;
import com.skybory.seoulArt.domain.seat.repository.SeatRepository;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketRequest;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketResponse;
import com.skybory.seoulArt.domain.ticket.dto.TicketDetailResponse;
import com.skybory.seoulArt.domain.ticket.entity.Ticket;
import com.skybory.seoulArt.domain.ticket.repository.TicketRepository;
import com.skybory.seoulArt.domain.user.UserRepository;
import com.skybory.seoulArt.domain.user.entity.User;
import com.skybory.seoulArt.global.SeatStatus;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketServiceImpl implements TicketService {

	private final TicketRepository ticketRepository;
	private final UserRepository userRepository;
	private final EventRepository eventRepository;
	private final SeatRepository seatRepository;

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

	@Override
	@Transactional 
	public CreateTicketResponse create(CreateTicketRequest request) {
		// 빈 자리 확인(boolean), 빈자리 없을시 오류 던짐
		hasAvailableSeats(request.getEventIdx());
		long seatIdx = 0;
		// eventIdx == request.getEventIdx() 인, 자리 중에 빈 자리 하나(seatIdx가 낮은 순서부터) 예약중으로 바꿔야함.
		if (request.getEventIdx()==1) {
			for(long i = 1; i<50; i++) {
			Seat seat =	seatRepository.findById(i).orElseThrow();
	       
			if (seat.getSeatStatus() == SeatStatus.AVAILABLE) {
	            seat.setSeatStatus(SeatStatus.RESERVING);
	            seatIdx = seat.getSeatIdx();
	            break; // 예약된 좌석을 찾으면 루프 종료
				}
			}
		}
		
		if (request.getEventIdx()==2) {
			
		}
		
		if (request.getEventIdx()==3) {
			
		}
		
		// 티켓 생성
		Ticket ticket = new Ticket();
		// 매핑 request -> ticket
		mapping(request, ticket,seatIdx);
		// DB저장
		ticketRepository.save(ticket);
		// DTO 반환
		CreateTicketResponse response = new CreateTicketResponse();
		// 매핑 request -> response
		response.setEventIdx(request.getEventIdx());
		response.setSeatIdx(seatIdx);
		response.setUserIdx(request.getUserIdx());
		return response;
	}
	

	// 공연 취소하기
	@Override
	@Transactional
	public void deleteTicket(long userId) {
		// 해당 사용자의 특정 이벤트에 대한 티켓을 찾아서 삭제
		User user = userRepository.findById(userId).orElseThrow();
		long ticketIdx = user.getTicket().getTicketIdx();

		user.setTicket(null);
		userRepository.save(user); // 변경된 상태를 데이터베이스에 반영

		ticketRepository.findById(ticketIdx).get().getSeat().setSeatStatus(SeatStatus.AVAILABLE);
		ticketRepository.deleteById(ticketIdx);
	}


	
//	private void existTicket(final long ticketId) {
//		if(ticketRepository.existsById(ticketId)) {
//			throw new ServiceException(ErrorCode.DUPLICATE_TICKET);
//		}
//	}


	@Transactional
	private void mapping(CreateTicketRequest request, Ticket ticket, long seatIdx) {
		// 사용자, 이벤트 및 좌석 정보 조회
		User user = userRepository.findById(request.getUserIdx()) .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		Event event = eventRepository.findById(request.getEventIdx()) .orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
		Seat seat = seatRepository.findById(seatIdx) .orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));

		ticket.setSeat(seat);
		ticket.setUser(user);
		ticket.changeEvent(event);
		user.setTicket(ticket);
	}
	
	// 빈 자리 확인
	private void hasAvailableSeats(long eventIdx) {
//		Event event = eventRepository.findById(eventIdx) .orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
		// 총 좌석 수
		int totalSeats = 50;
		int bookedSeats = (int) ticketRepository.countByEventEventIdx(eventIdx);
		System.out.println("bookedSeats : " + bookedSeats);
		if ( totalSeats <= bookedSeats) {
			throw new ServiceException(ErrorCode.SEAT_UNAVAILABLE);
		}
	}


	@Override
	public TicketDetailResponse findTicket(long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		Ticket ticket = user.getTicket();
		Event event = ticket.getEvent();
		Seat seat = ticket.getSeat();
		
		TicketDetailResponse response = new TicketDetailResponse();
		// dto 매핑
		response.setEventDetail(event.getDetail());
		response.setEventIdx(event.getEventIdx());
		response.setEventImage(event.getImage());
		response.setEventTitle(event.getTitle());
		
		response.setSeatIdx(seat.getSeatIdx());
		response.setSeatStatus(seat.getSeatStatus());
		
		response.setUserId(userId);
		response.setUsername(user.getUsername());
		
		response.setTicketIdx(ticket.getTicketIdx());
		
		return response;
	}

 
	
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