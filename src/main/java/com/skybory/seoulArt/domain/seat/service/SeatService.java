package com.skybory.seoulArt.domain.seat.service;

import java.util.List;
import com.skybory.seoulArt.domain.seat.dto.CreateSeatRequest;
import com.skybory.seoulArt.domain.seat.dto.CreateSeatResponse;


public interface SeatService {

	List<CreateSeatResponse> createSeats(CreateSeatRequest createSeatDTO);

	void deleteSeat(long seatIdx);
}