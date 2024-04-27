package com.skybory.seoulArt.domain.seat.service;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.skybory.seoulArt.domain.seat.dto.CreateSeatRequest;
import com.skybory.seoulArt.domain.seat.dto.CreateSeatResponse;
import com.skybory.seoulArt.domain.seat.entity.Seat;
import com.skybory.seoulArt.domain.seat.repository.SeatRepository;
import com.skybory.seoulArt.global.SeatStatus;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatServiceImpl implements SeatService {

	private final SeatRepository seatRepository;

	@Override	// 좌석 생성 : 이벤트당 한번만 해야함
	@Transactional
	public List<CreateSeatResponse> createSeats(CreateSeatRequest request) {
		
		// 좌석 생성하기
		int amount = request.getAmount();
		long eventId = request.getEventId();
		List<Seat> seatList = new ArrayList<>();

		// 1~3번 이벤트까지 한번에 생성
//		for (int j = 1; j <= 3; j++) {
			
			for(long i=1; i<=amount; i++) {
			Seat seat = new Seat();
					seat.setSeatStatus(SeatStatus.AVAILABLE);
//					seat.setSeatIdx(i);
					seat.setEventIdx(request.getEventId());
					seatList.add(seat);
//					seatRepository.save(seat);
				}
//		}
		seatRepository.saveAll(seatList);

		// DTO로 옮겨담기
		List<CreateSeatResponse> responseList = seatList.stream()
				.map(seat -> {
					CreateSeatResponse response = new CreateSeatResponse();
					// 매핑
					response.setEventId(eventId);
					response.setSeatId(seat.getSeatIdx());
					return response;
				})
				.collect(Collectors.toList());
		return responseList;
	}
	
	@Override
	@Transactional
	public void deleteSeat(long seatIdx) {
		seatRepository.deleteById(seatIdx);
	}

//	private String generateSeatNumber(int column, int row) {
//		// 열(column)을 알파벳으로 변환
//		String columnAlphabet = convertToAlphabet(column);
//
//		// SeatNumber를 생성하여 반환
//		return columnAlphabet + row;
//	}

//	private String convertToAlphabet(int column) {
//		// ASCII 코드를 사용하여 숫자를 알파벳으로 변환
//		return String.valueOf((char) (column + 64)); // 'A'는 65에 해당함
//	}


}
