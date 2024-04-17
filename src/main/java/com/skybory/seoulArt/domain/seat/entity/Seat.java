package com.skybory.seoulArt.domain.seat.entity;

import com.skybory.seoulArt.domain.seat.SeatStatus;
import com.skybory.seoulArt.domain.ticket.entity.Ticket;

import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
		name="SEAT_SEQ_GENERATOR",
		sequenceName = "SEAT,SEQ",
		initialValue = 1, allocationSize = 1
		)
public class Seat {
	
	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "SEAT_SEQ_GENERATOR"
			)
	private Long seatIdx;		// 식별을 위한 PK값
	
//	private String seatNumber;	// 이제는 필요없어진..
	
	private SeatStatus seatStatus;	// 좌석 상태 표기 : 예매가능
	
	@OneToOne(mappedBy = "seat")	// ticket 에게 주인 권한을 줌
	private Ticket ticket;
	
//	@ManyToOne(mappedBy = "seat")
//	private Event event;

	
}
