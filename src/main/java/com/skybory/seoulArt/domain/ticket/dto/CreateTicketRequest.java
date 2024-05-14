package com.skybory.seoulArt.domain.ticket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTicketRequest {

	// 티켓 생성할때 사용하는 DTO (요청)
	
//	private long ticketIdx; 

	private Long eventIdx;	// 몇번 공연을 예매할건지

//	private Long userIdx;	// 누가 예매할건지

//	private long seatIdx;

}
