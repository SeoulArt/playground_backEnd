package com.skybory.seoulArt.domain.ticket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTicketResponse {
	
	// 티켓 생성할때 사용하는 DTO (응답)
	
//	private long ticketIdx;
	
	private long eventIdx;

	private long userIdx;
	
	private long seatIdx;
	
}
