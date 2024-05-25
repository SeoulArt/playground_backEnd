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
	public List<CreateSeatResponse> createSeats() {
		
		// 좌석 생성하기
//		int amount = request.getAmount();
//		long eventId = request.getEventId();
		List<Seat> seatList = new ArrayList<>();

		// 1~3번 이벤트까지 한번에 생성
		for (Long j = 1L; j <= 6; j++) {
		    long seatCount; // 각 경우에 따라 생성할 좌석의 수를 저장할 변수
		    if (j == 1L || j == 2L) {
		        seatCount = 72; // j가 1 또는 2일 경우, 72개의 좌석 생성
		    } else if (j == 3L || j == 4L) {
		        seatCount = 60; // j가 3 또는 4일 경우, 60개의 좌석 생성
		    } else {
		        seatCount = 72; // j가 5 또는 6일 경우, 72개의 좌석 생성
		    }

		    for (Long i = 1L; i <= seatCount; i++) {
		        Seat seat = new Seat();
		        seat.setSeatStatus(SeatStatus.AVAILABLE); // 모든 좌석을 'AVAILABLE' 상태로 설정
		        seat.setPlayIdx(j); // 이벤트 인덱스를 j로 설정
		        seatList.add(seat); // 생성된 좌석을 목록에 추가
		    }
		}
		seatRepository.saveAll(seatList);

		// DTO로 옮겨담기
		List<CreateSeatResponse> responseList = seatList.stream()
				.map(seat -> {
					CreateSeatResponse response = new CreateSeatResponse();
					// 매핑
//					response.setEventId(eventId);
					response.setPlayId(seat.getPlayIdx());
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
