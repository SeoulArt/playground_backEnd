package com.skybory.seoulArt.domain.ticket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveSeatResponse {
	
	// 티켓 생성할때 사용하는 DTO (응답)
	
//	private long ticketIdx;
	
	private Long playId;

//	private Long userIdx;
	
	private Long ticketId;
	
	private Long seatId;
	
}
