package com.skybory.seoulArt.domain.ticket.controller;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skybory.seoulArt.domain.ticket.dto.CompleteBookingRequest;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketRequest;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketResponse;
import com.skybory.seoulArt.domain.ticket.dto.DeleteTicketRequest;
import com.skybory.seoulArt.domain.ticket.dto.ReserveSeatRequest;
import com.skybory.seoulArt.domain.ticket.dto.ReserveSeatResponse;
import com.skybory.seoulArt.domain.ticket.dto.TicketDetailResponse;
import com.skybory.seoulArt.domain.ticket.dto.TicketListResponse;
import com.skybory.seoulArt.domain.ticket.service.TicketService;
import com.skybory.seoulArt.domain.user.dto.UserDTO;
import com.skybory.seoulArt.domain.user.service.UserService;
import com.skybory.seoulArt.global.exception.ApiError;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
//@CrossOrigin(origins = "http://localhost:5174")
@Log4j2
@RequiredArgsConstructor // 생성자 생성(의존성주입)
@RequestMapping("/api/ticket")
public class TicketController {

	private final TicketService ticketService;
	
//	 '예약하기 버튼' 누를 때 -> 예매완료 화면으로 이동.
	@PostMapping("")	
	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
	@Operation(summary = "티켓 예매하기", description = "티켓을 예매합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="커스텀 에러 발생", content = @Content(schema = @Schema(implementation = ApiError.class)))
	public ResponseEntity<Object> create(@RequestBody CreateTicketRequest request, HttpServletRequest requestServlet) {
//		return ResponseEntity.ok(ticketService.create(request,requestServlet));
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime activationTime = LocalDateTime.of(2024, Month.MAY, 27, 22, 4, 59);
		
	    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
	    ZonedDateTime activationTime = ZonedDateTime.of(2024, 5, 28, 16, 41, 50, 0, ZoneId.of("Asia/Seoul"));
	    System.out.println(now);

        if (now.isBefore(activationTime)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("예매 시작 전입니다.");
        }
        else {
        	return ResponseEntity.ok(ticketService.create(request,requestServlet));
        }

	}


	@DeleteMapping("")	// 포스트맨 성공 0418
	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
	@Operation(summary = "예매 취소", description = "예매 완료된 티켓의 예매를 취소합니다(티켓 삭제!)")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<String> deleteTicket(@RequestBody DeleteTicketRequest request, HttpServletRequest requestServlet) {
		log.info("deleteTicket 호출");
		ticketService.deleteTicket(request.getTicketId(),requestServlet); 
//		System.out.println("userId : " + currentUser.getUserId());
		return ResponseEntity.ok("예약이 성공적으로 취소되었습니다");
	}
	


	@GetMapping("/available")	// 포스트맨 성공 0418.	-> ticketId
	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
	@Operation(summary = "예매 가능 공연 확인", description = "예매 가능한 공연을 확인합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<List<Long>> getAvailableList() {
		log.info("예매 가능 리스트 확인");
		return ResponseEntity.ok(ticketService.getAvailableList());
	}
}


//	@PostMapping("/reserve")	// 포스트맨 성공 0417. 그러나 추가적인 에러 테스트가 필요해보임( 제일 중요한 로직 )
//	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
//	@Operation(summary = "티켓 예매시작", description = "티켓을 예매시작합니다. 좌석을 예약중으로 바꾸고, 전화번호 입력을 기다립니다")
//	@ApiResponse(responseCode="200", description="성공")
//	@ApiResponse(responseCode="400", description="커스텀 에러 발생", content = @Content(schema = @Schema(implementation = ApiError.class)))
//	public ResponseEntity<ReserveSeatResponse> reserve(@RequestBody ReserveSeatRequest request) {
//		return ResponseEntity.ok(ticketService.reserveSeat(request));
//	}
//	
//	@PostMapping("/complete")	// 포스트맨 성공 0417. 그러나 추가적인 에러 테스트가 필요해보임( 제일 중요한 로직 )
//	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
//	@Operation(summary = "티켓 예매 완료하기", description = "티켓을 예매 완료합니다. 전화번호가 입력되지 않았을 시, 티켓을 취소합니다")
//	@ApiResponse(responseCode="200", description="성공")
//	@ApiResponse(responseCode="400", description="커스텀 에러 발생", content = @Content(schema = @Schema(implementation = ApiError.class)))
//	public ResponseEntity<String> complete(@RequestBody CompleteBookingRequest request) {
//		return ResponseEntity.ok(ticketService.completeBooking(request));
//	}
	// '예약확인'
//	@GetMapping("/{ticketId}")	// 포스트맨 성공 0418.	-> ticketId
//	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
//	@Operation(summary = "예매 확인", description = "해당 유저의 티켓을 조회합니다")
//	@ApiResponse(responseCode="200", description="성공")
//	@ApiResponse(responseCode="400", description="에러")
//	public ResponseEntity<TicketDetailResponse> find(HttpServletRequest request, @PathVariable Long ticketId) {
//		log.info("예약 확인");
//		UserDTO user = userService.getCurrentUser(request);
//		return ResponseEntity.ok(ticketService.findTicket(user.getUserId(), ticketId));
//	}	
	
//	@GetMapping("")	// 포스트맨 성공 0418.	-> ticketId
//	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
//	@Operation(summary = "예매 리스트 확인", description = "해당 유저의 티켓 리스트를 조회합니다")
//	@ApiResponse(responseCode="200", description="성공")
//	@ApiResponse(responseCode="400", description="에러")
//	public ResponseEntity<TicketListResponse> getList() {
//		log.info("예매 리스트 확인");
//		return ResponseEntity.ok(ticketService.getTicketList());
//	}

 