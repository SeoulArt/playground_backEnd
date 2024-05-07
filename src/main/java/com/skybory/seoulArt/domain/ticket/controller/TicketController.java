package com.skybory.seoulArt.domain.ticket.controller;

import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skybory.seoulArt.domain.event.controller.EventController;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketRequest;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketResponse;
import com.skybory.seoulArt.domain.ticket.dto.TicketDetailResponse;
import com.skybory.seoulArt.domain.ticket.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
//@CrossOrigin(origins = "http://localhost:5174")
@Log4j2
@RequiredArgsConstructor // 생성자 생성(의존성주입)
@RequestMapping("/api/ticket")
public class TicketController {

	private final TicketService ticketService;
	
	// '예약하기 버튼' 누를 때 -> 예매완료 화면으로 이동.
	@PostMapping("/new")	// 포스트맨 성공 0417. 그러나 추가적인 에러 테스트가 필요해보임( 제일 중요한 로직 )
	@Operation(summary = "티켓 예매하기", description = "티켓을 예매합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<CreateTicketResponse> create(@RequestBody CreateTicketRequest request) {
		return ResponseEntity.ok(ticketService.create(request));
	}

	
	@DeleteMapping("/{userId}")	// 포스트맨 성공 0418
	@Operation(summary = "예매 취소", description = "예매 완료된 티켓의 예매를 취소합니다(티켓 삭제!)")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<String> deleteTicket(@Parameter(description = "유저 id")@PathVariable Long userId) {
		ticketService.deleteTicket(userId);
		return ResponseEntity.ok("Ticket successfully deleted");
	}
	
	// '예약확인'
	@GetMapping("/find/{userId}")	// 포스트맨 성공 0418.
	@Operation(summary = "예매 확인", description = "해당 유저의 티켓을 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<TicketDetailResponse> find(@Parameter(description = "유저 id")@PathVariable Long userId) {
		return ResponseEntity.ok(ticketService.findTicket(userId));
	}
	

}
 