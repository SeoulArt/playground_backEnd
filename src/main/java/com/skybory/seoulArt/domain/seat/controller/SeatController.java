package com.skybory.seoulArt.domain.seat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skybory.seoulArt.domain.event.controller.EventController;
import com.skybory.seoulArt.domain.seat.dto.CreateSeatRequest;
import com.skybory.seoulArt.domain.seat.dto.CreateSeatResponse;
import com.skybory.seoulArt.domain.seat.service.SeatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
//@CrossOrigin(origins = "http://reactserver")
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/seat")
public class SeatController {

	// 이거 자체를 지우자
	
	private final SeatService seatService;
	@Secured("ROLE_ADMIN")
	@PostMapping("")		// 포스트맨 테스트 완료 0417
	@Operation(summary = "좌석 생성", description = "티켓 예약이 가능한 좌석을 생성합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<List<CreateSeatResponse>> createSeats(@RequestBody CreateSeatRequest request){
		// 좌석 생성
//		seatService.createSeats(createSeatDTO);
		return ResponseEntity.ok(seatService.createSeats(request));
	}
	
	@DeleteMapping("/{seatIdx}")	// 포스트맨 테스트 완료 0417
	@Operation(summary = "좌석 삭제", description = "좌석을 삭제합니다. delete on Cascade test 해봐야함")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<String> deleteSeat(@PathVariable long seatIdx) {
		// 좌석 삭제
		seatService.deleteSeat(seatIdx);
		return ResponseEntity.ok("Seat successfully deleted");
	}
}
