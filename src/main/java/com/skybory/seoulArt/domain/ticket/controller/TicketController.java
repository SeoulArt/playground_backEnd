package com.skybory.seoulArt.domain.ticket.controller;

import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketRequest;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketResponse;
import com.skybory.seoulArt.domain.ticket.dto.TicketDetailResponse;
import com.skybory.seoulArt.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;

@RestController
//@CrossOrigin(origins = "http://localhost:5174")
@RequiredArgsConstructor // 생성자 생성(의존성주입)
@RequestMapping("/api/ticket")
public class TicketController {

	private final TicketService ticketService;
	
	// '예약하기 버튼' 누를 때 -> 예매완료 화면으로 이동.
	@PostMapping("/create")	// 포스트맨 성공 0417. 그러나 추가적인 에러 테스트가 필요해보임( 제일 중요한 로직 )
	public ResponseEntity<CreateTicketResponse> create(@RequestBody CreateTicketRequest request) {
		return ResponseEntity.ok(ticketService.create(request));
	}

	
	@DeleteMapping("/delete/{userId}")	// 포스트맨 성공 0418
	public ResponseEntity<String> deleteTicket(@PathVariable long userId) {
		ticketService.deleteTicket(userId);
		return ResponseEntity.ok("Ticket successfully deleted");
	}
	
	// '예약확인'
	@GetMapping("/find/{userId}")	// 포스트맨 성공 0418.
	public ResponseEntity<TicketDetailResponse> find(@PathVariable long userId) {
		return ResponseEntity.ok(ticketService.findTicket(userId));
	}
	

}
 