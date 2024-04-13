package com.skybory.seoulArt.domain.seat;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SeatDTO {

	private int seatIdx;		// PK값
	private String seatNumber;
	private int userIdx;
	
}
