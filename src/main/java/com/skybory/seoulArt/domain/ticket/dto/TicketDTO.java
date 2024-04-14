package com.skybory.seoulArt.domain.ticket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TicketDTO {
	
	private long ticketIdx;	//pk값
	
//	private PlayDTO pdto;
	private long eventIdx;
	
//	private UserDTO udto;
	private long userIdx;
	
//	private SeatDTO sdto;
	private long seatIdx;
}
