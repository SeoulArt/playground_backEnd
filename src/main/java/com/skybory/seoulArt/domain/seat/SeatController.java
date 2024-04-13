package com.skybory.seoulArt.domain.seat;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://reactserver")
@RequiredArgsConstructor
@RequestMapping("/api/seat")
public class SeatController {

	private final SeatService seatService;
	
	@PostMapping("/create/seats")	// 정상 작동 확인 완료!!
	public ResponseEntity<List<Seat>> createSeats(@RequestBody CreateSeatDTO createSeatDTO){
		
		// 좌석 생성
		List<Seat> newSeats = seatService.createSeats(createSeatDTO);
		
		return ResponseEntity.ok(newSeats);
	}
	
	@DeleteMapping("delete/seat/{seatIdx}")	// 정상 작동 확인완료!
	public ResponseEntity<String> deleteSeat(@PathVariable long seatIdx) {
		// 좌석 삭제
		seatService.deleteSeat(seatIdx);
		return ResponseEntity.ok("Seat successfully deleted");
	}
}
