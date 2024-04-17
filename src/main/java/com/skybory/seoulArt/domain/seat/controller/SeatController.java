package com.skybory.seoulArt.domain.seat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.skybory.seoulArt.domain.seat.dto.CreateSeatRequest;
import com.skybory.seoulArt.domain.seat.dto.CreateSeatResponse;
import com.skybory.seoulArt.domain.seat.service.SeatService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://reactserver")
@RequiredArgsConstructor
@RequestMapping("/api/seat")
public class SeatController {

	private final SeatService seatService;
	
	@PostMapping("/create")		// 포스트맨 테스트 완료 0417
	public ResponseEntity<List<CreateSeatResponse>> createSeats(@RequestBody CreateSeatRequest request){
		// 좌석 생성
//		seatService.createSeats(createSeatDTO);
		return ResponseEntity.ok(seatService.createSeats(request));
	}
	
	@DeleteMapping("delete/{seatIdx}")	// 포스트맨 테스트 완료 0417
	public ResponseEntity<String> deleteSeat(@PathVariable long seatIdx) {
		// 좌석 삭제
		seatService.deleteSeat(seatIdx);
		return ResponseEntity.ok("Seat successfully deleted");
	}
}
