package com.skybory.seoulArt.domain.ticket.service;

import com.skybory.seoulArt.domain.ticket.dto.CreateTicketRequest;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketResponse;
import com.skybory.seoulArt.domain.ticket.dto.TicketDetailResponse;
import com.skybory.seoulArt.domain.ticket.entity.Ticket;

public interface TicketService {
	
	// 티켓 만들기(=공연 예약하기)
//	Ticket createTicket(Long userIdx, Long eventIdx, Long seatIdx);

    // 티켓 삭제하기
	void deleteTicket(long userId);

	// 0413 level up
//	CreateTicketResponse createTicket(CreateTicketRequest request);

	
//	Long create(final Ticket ticket) ;
//    //공연 예매가능한지 확인
//    boolean checkAvailability(Event event);

	CreateTicketResponse create(CreateTicketRequest request);

	TicketDetailResponse findTicket(long userId);

//	Object findById(long ticketId);

}
