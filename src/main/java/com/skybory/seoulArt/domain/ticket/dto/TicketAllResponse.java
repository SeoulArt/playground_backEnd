package com.skybory.seoulArt.domain.ticket.dto;

import com.skybory.seoulArt.domain.ticket.entity.Ticket;

public class TicketAllResponse {

	private Long ticketId;
	
	private long userId;
	
	// 티켓의 모든것을 가져오는 메서드. 필요시 마저 작성
//	public static TicketAllResponse of(final Ticket ticket, final long userId) {
//		return new TicketAllResponse(ticket.getUser(), ticket.getEvent())
//	}
}
