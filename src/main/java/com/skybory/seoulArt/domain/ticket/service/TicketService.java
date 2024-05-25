package com.skybory.seoulArt.domain.ticket.service;

import java.util.List;

import com.skybory.seoulArt.domain.ticket.dto.CompleteBookingRequest;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketRequest;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketResponse;
import com.skybory.seoulArt.domain.ticket.dto.ReserveSeatRequest;
import com.skybory.seoulArt.domain.ticket.dto.ReserveSeatResponse;
import com.skybory.seoulArt.domain.ticket.dto.TicketDetailResponse;
import com.skybory.seoulArt.domain.ticket.dto.TicketListResponse;
import com.skybory.seoulArt.domain.ticket.entity.Ticket;

import jakarta.servlet.http.HttpServletRequest;

public interface TicketService {
	



	TicketListResponse getTicketList();

	List<Long> getAvailableList();

//	CreateTicketResponse create(CreateTicketRequest request);
	CreateTicketResponse create(CreateTicketRequest request, HttpServletRequest requestServlet);


	// 티켓 삭제하기
//	void deleteTicket(Long ticketId);
	void deleteTicket(Long ticketId, HttpServletRequest requestServlet);


}
