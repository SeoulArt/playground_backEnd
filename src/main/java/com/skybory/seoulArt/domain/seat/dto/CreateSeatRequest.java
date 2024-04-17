package com.skybory.seoulArt.domain.seat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSeatRequest {

	private int amount;
	private long eventId;
}
