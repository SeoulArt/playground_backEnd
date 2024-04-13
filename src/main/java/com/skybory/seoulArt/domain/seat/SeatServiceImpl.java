package com.skybory.seoulArt.domain.seat;

import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

	private final SeatRepository seatRepository;

	@Override	// 좌석 생성 : 이벤트당 한번만 해야함
	public List<Seat> createSeats(CreateSeatDTO createSeatDTO) {
		
		int columns = createSeatDTO.getColumns();
		int rows = createSeatDTO.getRows();
		List<Seat> seats = new ArrayList<>();

		for (int row = 1; row <= rows; row++) {
			for (int column = 1; column <= columns; column++) {
				Seat seat = new Seat();
				seat.setSeatStatus(SeatStatus.AVAILABLE);
				seat.setSeatNumber(generateSeatNumber(column, row));
				seats.add(seat);
			}
		}

		return seatRepository.saveAll(seats);
	}
	
	@Override
	public void deleteSeat(long seatIdx) {
		seatRepository.deleteById(seatIdx);
	}

	private String generateSeatNumber(int column, int row) {
		// 열(column)을 알파벳으로 변환
		String columnAlphabet = convertToAlphabet(column);

		// SeatNumber를 생성하여 반환
		return columnAlphabet + row;
	}

	private String convertToAlphabet(int column) {
		// ASCII 코드를 사용하여 숫자를 알파벳으로 변환
		return String.valueOf((char) (column + 64)); // 'A'는 65에 해당함
	}


}
