package com.skybory.seoulArt.domain.ticket.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skybory.seoulArt.domain.ticket.dto.CreateTicketRequest;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketResponse;
import com.skybory.seoulArt.domain.ticket.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
//@CrossOrigin(origins = "http://localhost:5174")
@RequiredArgsConstructor // 생성자 생성(의존성주입)
@RequestMapping("/api/ticket")
public class TicketController {

	// 인터페이스를 호출하면 구현체가 같이 호출됨
	private final TicketService ticketService;

	// 예매서비스(figma) 홈
	// 이 부분 어떻게 고칠지 생각해봐야함.
//	@GetMapping("/main")
//	public ResponseEntity<Long> main(@RequestBody Long eventIdx) {
//		return eventIdx;
//	}
	
	// '예약하기 버튼' 누를 때 -> 예매완료 화면으로 이동
	@PostMapping("/create")
	public ResponseEntity<CreateTicketResponse> create(@RequestBody CreateTicketRequest request) {
		return ResponseEntity.ok(ticketService.createTicket(request));
	}

//	// '예약확인'
//	@GetMapping("/find")
//	public ResponseEntity<CreateTicketResponse> afterCreate(@RequestBody long ticketId) {
//		return ResponseEntity.ok(ticketService.findById(ticketId));
//	}
	
	

//		// 매핑
//		long userIdx = ticketDTO.getUserIdx();
//		long eventIdx = ticketDTO.getEventIdx();
//		long seatIdx = ticketDTO.getSeatIdx();
//		
//		// 티켓 생성 서비스 호출
//		Ticket newTicket = ticketService.createTicket(userIdx, eventIdx, seatIdx);

//		 return ResponseEntity.ok(newTicket);

	@DeleteMapping("/delete/{ticketIdx}")
	public ResponseEntity<String> deleteTicket(@PathVariable long ticketIdx) {
		
		// 티켓 삭제
		ticketService.deleteTicket(ticketIdx);
		
		return ResponseEntity.ok("Ticket successfully deleted");
	}

}
 