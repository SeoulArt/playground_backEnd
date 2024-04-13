package com.skybory.seoulArt.domain.seat;

import java.util.List;


public interface SeatService {

	List<Seat> createSeats(CreateSeatDTO createSeatDTO);

	void deleteSeat(long seatIdx);
}