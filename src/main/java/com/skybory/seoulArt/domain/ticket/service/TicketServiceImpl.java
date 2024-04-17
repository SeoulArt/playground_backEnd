package com.skybory.seoulArt.domain.ticket.service;

import org.springframework.stereotype.Service;

import com.skybory.seoulArt.domain.event.entity.Event;
import com.skybory.seoulArt.domain.event.repository.EventRepository;
import com.skybory.seoulArt.domain.seat.SeatStatus;
import com.skybory.seoulArt.domain.seat.entity.Seat;
import com.skybory.seoulArt.domain.seat.repository.SeatRepository;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketRequest;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketResponse;
import com.skybory.seoulArt.domain.ticket.entity.Ticket;
import com.skybory.seoulArt.domain.ticket.repository.TicketRepository;
import com.skybory.seoulArt.domain.user.UserRepository;
import com.skybory.seoulArt.domain.user.entity.User;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

	private final TicketRepository ticketRepository;
	private final UserRepository userRepository;
	private final EventRepository eventRepository;
	private final SeatRepository seatRepository;

	// 공연 예약하기
	@Override
	public Ticket createTicket(Long userIdx, Long eventIdx, Long seatIdx) {

		// 사용자, 이벤트 및 좌석 정보 조회
		User user = userRepository.findById(userIdx) .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		Event event = eventRepository.findById(eventIdx) .orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
		Seat seat = seatRepository.findById(seatIdx) .orElseThrow(() -> new ServiceException(ErrorCode.SEAT_NOT_FOUND));

		// 티켓 생성 로직
		Ticket ticket = new Ticket();
		ticket.setUser(user);
		ticket.changeEvent(event);
		ticket.setSeat(seat);
		ticket.getSeat().setSeatStatus(SeatStatus.RESERVED);
		// 티켓 저장 및 반환
		return ticketRepository.save(ticket);
	}

	@Override	// 무슨값 반환할지 못정했음
	public Long create(CreateTicketRequest createTicketRequest) {
		// 빈 자리 확인(boolean), 빈자리 없을시 오류 던짐
		hasAvailableSeats(createTicketRequest.getEventIdx());
		
		// 티켓 생성
		Ticket ticket = new Ticket();
		
		// 매핑
		mapping(createTicketRequest, ticket);
		
		// DB저장
		ticketRepository.save(ticket);
		
		return ticket.getTicketIdx();
	}
	

	// 공연 취소하기
	@Override
	public void deleteTicket(Long ticketIdx) {
		// 해당 사용자의 특정 이벤트에 대한 티켓을 찾아서 삭제
		ticketRepository.findById(ticketIdx).get().getSeat().setSeatStatus(SeatStatus.AVAILABLE);
		ticketRepository.deleteById(ticketIdx);
	}

	@Override
	public CreateTicketResponse createTicket(CreateTicketRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	
//	private void existTicket(final long ticketId) {
//		if(ticketRepository.existsById(ticketId)) {
//			throw new ServiceException(ErrorCode.DUPLICATE_TICKET);
//		}
//	}


	
	private void mapping(CreateTicketRequest request, Ticket ticket) {
		// 사용자, 이벤트 및 좌석 정보 조회
		User user = userRepository.findById(request.getUserIdx()) .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		Event event = eventRepository.findById(request.getEventIdx()) .orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));
		Seat seat = seatRepository.findById(request.getSeatIdx()) .orElseThrow(() -> new ServiceException(ErrorCode.EVENT_NOT_FOUND));

		ticket.setSeat(seat);
		ticket.setUser(user);
		ticket.changeEvent(event);
	}
	
	// 빈 자리 확인
	private void hasAvailableSeats(long eventIdx) {
		Event event = eventRepository.findById(eventIdx) .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		// 총 좌석 수
		int totalSeats = 50;
		int bookedSeats = (int) ticketRepository.count();
		
		if ( totalSeats <= bookedSeats) {
			throw new ServiceException(ErrorCode.SEAT_UNAVAILABLE);
		}
	}

	@Override
	public Long create(Ticket ticket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findById(long ticketId) {
		// TODO Auto-generated method stub
		return null;
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