package com.skybory.seoulArt.domain.ticket.dto;

import com.skybory.seoulArt.global.SeatStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDetailResponse {

	// 공연 정보
	private Long playIdx;
	private String playTitle;
	private String playDetail;
	private String playImage;
	
	// 좌석 정보
//	private long seatIdx;
//	private SeatStatus seatStatus;
	
	// 유저 정보
//	private String username;
//	private long userId;
	
	// 티켓 정보
	private Long ticketIdx;
	
	// 티켓의 모든것을 가져오는 메서드. 필요시 마저 작성
//	public static TicketAllResponse of(final Ticket ticket, final long userId) {
//		return new TicketAllResponse(ticket.getUser(), ticket.getplay())
//	}
}
