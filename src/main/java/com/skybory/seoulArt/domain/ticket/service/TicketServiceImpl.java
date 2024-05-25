package com.skybory.seoulArt.domain.ticket.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skybory.seoulArt.Oauth.JwtUtil;
import com.skybory.seoulArt.domain.play.entity.Play;
import com.skybory.seoulArt.domain.play.repository.PlayRepository;
//import com.skybory.seoulArt.domain.reply.service.ReplyServiceImpl;
import com.skybory.seoulArt.domain.seat.entity.Seat;
import com.skybory.seoulArt.domain.seat.repository.SeatRepository;
import com.skybory.seoulArt.domain.ticket.dto.CompleteBookingRequest;
import com.skybory.seoulArt.domain.ticket.dto.CompleteBookingResponse;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketRequest;
import com.skybory.seoulArt.domain.ticket.dto.CreateTicketResponse;
import com.skybory.seoulArt.domain.ticket.dto.ReserveSeatRequest;
import com.skybory.seoulArt.domain.ticket.dto.ReserveSeatResponse;
import com.skybory.seoulArt.domain.ticket.dto.TicketDetailResponse;
import com.skybory.seoulArt.domain.ticket.dto.TicketListResponse;
import com.skybory.seoulArt.domain.ticket.entity.Ticket;
import com.skybory.seoulArt.domain.ticket.repository.TicketRepository;
import com.skybory.seoulArt.domain.user.dto.UserDTO;
import com.skybory.seoulArt.domain.user.entity.User;
import com.skybory.seoulArt.domain.user.repository.UserRepository;
import com.skybory.seoulArt.global.SeatStatus;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class TicketServiceImpl implements TicketService {

	private final TicketRepository ticketRepository;
	private final UserRepository userRepository;
	private final PlayRepository playRepository;
	private final SeatRepository seatRepository;
	private final JwtUtil jwtUtil;

	//0520 주석
	@Transactional
	@Override
	public CreateTicketResponse create(CreateTicketRequest request, HttpServletRequest requestServlet) {
	    log.info("create 메서드 실행");
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
//	    Long userId = userDTO.getUserId();  // UserDTO에 userId가 있다고 가정	    
	    
		String bearerToken = requestServlet.getHeader("Authorization");
		String accessToken = bearerToken.substring(7);
		Long userId = jwtUtil.getUserIdFromToken(accessToken);
		
		//	    User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
	    
		Long playId = request.getPlayId();
		Play play = playRepository.findById(playId).orElseThrow(() -> new ServiceException(ErrorCode.PLAY_NOT_FOUND));
		
		// 유저가 티켓을 가지고있는지 판단
		log.info("canBook 실행");
		canBook(userId, play.getTitle());
		

		
		log.info("hasAvailableSeats 실행");
	    hasAvailableSeats(playId);

	    // 좌석 번호 지정
	    Long seatIdx = findAvailableSeat(playId);

	    // 티켓 생성
	    log.info("new Ticket() 실행");
	    Ticket ticket = new Ticket();
	    mapping(userId, ticket, seatIdx, playId);
	    ticketRepository.save(ticket);

	    CreateTicketResponse response = new CreateTicketResponse();
	    response.setPlayId(playId);
	    response.setTicketId(ticket.getTicketIdx()); 			// 이 부분이 에러날 수 있음
//	    response.setSeatIdx(seatIdx);
//	    response.setUserIdx(userId);

	    return response;
	}

//	@Transactional
//	@Override
//	public ReserveSeatResponse reserveSeat(ReserveSeatRequest request) {
//	    log.info("reserveSeat 메서드 실행");
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
//	    Long userId = userDTO.getUserId();
//	    
//
//	    Long playId = request.getPlayId();
//	    Play play = playRepository.findById(playId).orElseThrow(() -> new ServiceException(ErrorCode.PLAY_NOT_FOUND));
//	    
//	    log.info("canBook 실행");
//	    canBook(userId, play.getTitle());
//
//	    log.info("hasAvailableSeats 실행");
//	    hasAvailableSeats(playId);
//
//	    // 좌석 번호 지정 및 예약 상태 변경
//	    Long seatIdx = findAvailableSeat(playId);
//	    Seat seat = seatRepository.findById(seatIdx).orElseThrow(() -> new ServiceException(ErrorCode.SEAT_NOT_FOUND));
//	    seat.setSeatStatus(SeatStatus.RESERVING);
//	    seatRepository.save(seat);
//
//	    // 티켓 생성
//	    log.info("new Ticket() 실행");
//	    Ticket ticket = new Ticket();
//	    mapping(userId, ticket, seatIdx, playId);
//	    ticketRepository.save(ticket);
//
//	    ReserveSeatResponse response = new ReserveSeatResponse();
//	    response.setPlayId(playId);
//	    response.setTicketId(ticket.getTicketIdx());
//	    response.setSeatId(seatIdx);		// 이게 필요한가?
//
//	    return response;
//	}
	
//	@Transactional
//	@Override
//	public String completeBooking(CompleteBookingRequest request) {
//	    log.info("completeBooking 메서드 실행");
//	    Long ticketId = request.getTicketId();
//	    Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ServiceException(ErrorCode.TICKET_NOT_FOUND));
//	    Seat seat = ticket.getSeat();
//
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
//	    Long userId = userDTO.getUserId();
//	    User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
//	    
//	    if (user.getPhoneNumber() != null) {
//	        seat.setSeatStatus(SeatStatus.RESERVED);
//	        seatRepository.save(seat);
//	        return "Complete Booking with phoneNumber";
//	    } else {
//	        // 5분 후에 체크하여 전화번호가 여전히 입력되지 않았다면 예약 취소
//	        scheduleCancellation(seat.getSeatIdx(), ticketId);
//	        return "Cancle Booking in 1 Minutes";
//	    }
//	}
//
//	@Transactional
//	private void scheduleCancellation(Long seatId, Long ticketId) {
//	    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//	    log.info("scheduleCancellation 실행");
//	    scheduler.schedule(() -> {
//	        Seat seat = seatRepository.findById(seatId).orElse(null);
//	        if (seat != null && seat.getSeatStatus() == SeatStatus.RESERVING) {
//	        	deleteTicket(ticketId);
////	            ticketRepository.deleteById(ticketId);
//	            log.info("예약 취소됨: ticket ID " + ticketId);
//	        }
//	    }, 1, TimeUnit.MINUTES);
//	    scheduler.shutdown();
//	}
	
    public boolean canBook(Long userId, String newplayTitle) throws ServiceException {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        if (user.getTicketList() == null || user.getTicketList().isEmpty()) {
            return true;  // 티켓을 가지고 있지 않으면 예매 가능
        }
        
        // 이미 가지고 있는 티켓 중에서 새 이벤트 제목과 동일한 제목을 가진 티켓이 있는지 확인
        boolean hasDuplicateplayTicket = user.getTicketList().stream()
            .anyMatch(ticket -> ticket.getPlay().getTitle().equals(newplayTitle));

        if (hasDuplicateplayTicket) {
            throw new ServiceException(ErrorCode.DUPLICATE_TICKET);
        }

        return true;  // 동일한 제목의 티켓이 없으면 예매 가능
    }

	@Transactional
	public void mapping(Long userId, Ticket ticket, Long seatIdx, Long playId) {
		// 사용자, 이벤트 및 좌석 정보 조회
		User user = userRepository.findById(userId) .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		Play play = playRepository.findById(playId) .orElseThrow(() -> new ServiceException(ErrorCode.PLAY_NOT_FOUND));
		Seat seat = seatRepository.findById(seatIdx) .orElseThrow(() -> new ServiceException(ErrorCode.SEAT_NOT_FOUND));
		
		ticket.setSeat(seat);
		ticket.setUser(user);
		ticket.changePlay(play);
		ticket.changeUser(user);
//		user.setTickets.add(ticket);
	}
	
	
	public Long findAvailableSeat(Long playIdx) {
	    Long startSeatIdx;
	    Long endSeatIdx;
	    if (playIdx == 1L || playIdx == 2L) {
	        startSeatIdx = 1 + (playIdx - 1) * 72;  // 1번 공연과 2번 공연은 72개의 좌석을 가진다.
	        endSeatIdx = playIdx * 72;
	    } else if (playIdx == 3L || playIdx == 4L) {
	        startSeatIdx = 145 + (playIdx - 3) * 60;  // 3번 공연과 4번 공연은 60개의 좌석을 가진다.
	        endSeatIdx = 144 + (playIdx-2) * 60;
	    } else {
	        startSeatIdx = 265 + (playIdx - 5) * 72;  // 5번 공연과 6번 공연은 72개의 좌석을 가진다.
	        endSeatIdx = 264 + (playIdx-4) * 72;
	    }

	    for (Long i = startSeatIdx; i <= endSeatIdx; i++) {
	        Seat seat = seatRepository.findById(i).orElseThrow(() -> new ServiceException(ErrorCode.SEAT_NOT_FOUND));
	        if (seat.getSeatStatus() == SeatStatus.AVAILABLE) {
	            seat.setSeatStatus(SeatStatus.RESERVING);
	            return seat.getSeatIdx(); // 예약된 좌석 인덱스 반환
	        }
	    }
	    throw new ServiceException(ErrorCode.SEAT_UNAVAILABLE); // 빈 좌석이 없을 경우 예외 발생
	}
 
	
	
//	// 빈 자리 확인
//	public boolean hasAvailableSeats(Long playId) {
//	    int totalSeats;
//	    if (playId == 1L || playId == 2L) {
//	        totalSeats = 72;
//	    } else if (playId == 3L || playId == 4L) {
//	        totalSeats = 60;
//	    } else {
//	        totalSeats = 72;
//	    }
//
//	    int bookedSeats = (int) ticketRepository.countByPlayPlayId(playId);
//	    if (totalSeats <= bookedSeats) {
//	        throw new ServiceException(ErrorCode.SEAT_UNAVAILABLE);
//	    } else
//		return true;
//	}
	
	public boolean hasAvailableSeats(Long playId) {
//	    int totalSeats;
//	    if (playId == 1L || playId == 2L) {
//	        totalSeats = 72;
//	    } else if (playId == 3L || playId == 4L) {
//	        totalSeats = 60;
//	    } else {
//	        totalSeats = 72;
//	    }
	    // 데이터베이스에서 해당 playId를 가지고, seatStatus가 AVAILABLE인 좌석의 수를 카운트
	    int availableSeats = seatRepository.countByPlayIdxAndSeatStatus(playId, SeatStatus.AVAILABLE);
	    if (availableSeats > 0) {
	        return true; // 사용 가능한 좌석이 하나 이상 있다면 true 반환
	    } else {
	        return false; // 사용 가능한 좌석이 없다면 예외 발생
	    }
	}


	@Override
	@Transactional
	public void deleteTicket(Long ticketId, HttpServletRequest requestServlet) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
//	    Long userId = userDTO.getUserId();  // UserDTO에 userId가 있다고 가정	 
	    
		User user = findUserByHeader(requestServlet);

		List<Ticket> tickets = user.getTicketList();
		
		// 같은것찾기
	    Optional<Ticket> ticketOptional = tickets.stream()
	            .filter(ticket -> ticket.getTicketIdx().equals(ticketId))
	            .findFirst();

	        // 티켓이 존재하면 삭제
	        if (ticketOptional.isPresent()) {
	            Ticket ticketToDelete = ticketOptional.get();
	            Seat seat = ticketToDelete.getSeat();
	            // tickets.remove(ticketToDelete); // user의 티켓 목록에서 제거, 필요하다면
	            ticketRepository.delete(ticketToDelete); // ticketRepository를 통해 실제로 삭제
	            seat.setSeatStatus(SeatStatus.AVAILABLE);
	        } else {
	            throw new ServiceException(ErrorCode.TICKET_NOT_FOUND);
	        }
	    }

	@Override
	public TicketListResponse getTicketList() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
	    Long userId = userDTO.getUserId();  // UserDTO에 userId가 있다고 가정	 
		User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		
        List<Long> playIds = user.getTicketList().stream()
                .map(ticket -> ticket.getPlay().getPlayId())
                .collect(Collectors.toList());
		TicketListResponse response = new TicketListResponse();
		response.setTicketList(playIds);
		
		return response;
	}


	@Override
	public List<Long> getAvailableList() {
	    List<Long> availablePlays = new ArrayList<>(); // 사용 가능한 공연 번호를 저장할 리스트
	    for (Long i = 1L; i <= 6; i++) { // 1부터 6까지 반복
	        if (hasAvailableSeats(i)) { // 사용 가능한 자리가 있다면
	            availablePlays.add(i); // 리스트에 공연 번호 추가
	        }
	    }
	    return availablePlays; // 사용 가능한 공연 번호 리스트 반환
	}
	
//	public User findUserByCookie() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
//	    Long userId = userDTO.getUserId();  // UserDTO에 userId가 있다고 가정	    
//	    User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
//	    return user;
//	}

	public User findUserByHeader(HttpServletRequest requestServlet) {
		String bearerToken = requestServlet.getHeader("Authorization");
		String accessToken = bearerToken.substring(7);
		Long userId = jwtUtil.getUserIdFromToken(accessToken);
		User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		return user;
	}
	
	

//	@Override
//	public TicketDetailResponse getTicketDetail(Long ticketId) {
//		
//		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ServiceException(ErrorCode.TICKET_NOT_FOUND));
//		TicketDetailResponse response = new TicketDetailResponse();
//		Play play = playRepository.findById(ticket.getPlay().getPlayId());
//		// dto 매핑
//		response.setplayDetail(play.getDetail());
//		response.setplayIdx(play.getplayIdx());
//		response.setplayImage(play.getImage());
//		response.setplayTitle(play.getTitle());
//		
//		response.setSeatIdx(seat.getSeatIdx());
//		response.setSeatStatus(seat.getSeatStatus());
//		
//		response.setUserId(userId);
//		response.setUsername(user.getUsername());
//		
//		response.setTicketIdx(ticket.getTicketIdx());
//		
//		return response;
//	}

//	@Override
//	public CreateTicketResponse createTicket(CreateTicketRequest request) {
//		// 사용자, 이벤트 및 좌석 정보 조회
//		User user = userRepository.findById(request.getUserIdx()) .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
//		play play = playRepository.findById(request.getplayIdx()) .orElseThrow(() -> new ServiceException(ErrorCode.play_NOT_FOUND));
//		Seat seat = seatRepository.findById(request.getSeatIdx()) .orElseThrow(() -> new ServiceException(ErrorCode.SEAT_NOT_FOUND));
//
//		// 티켓 생성 로직
//		Ticket ticket = new Ticket();
//		ticket.setUser(user);
//		ticket.changeplay(play);
//		ticket.setSeat(seat);
//		ticket.getSeat().setSeatStatus(SeatStatus.RESERVED);
//		// 티켓 저장 및 반환
//		return ticketRepository.save(ticket);
//	}
//	// 공연 예약하기
//	@Override
//	public Ticket createTicket(Long userIdx, Long playIdx, Long seatIdx) {
//
//		// 사용자, 이벤트 및 좌석 정보 조회
//		User user = userRepository.findById(userIdx) .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
//		play play = playRepository.findById(playIdx) .orElseThrow(() -> new ServiceException(ErrorCode.play_NOT_FOUND));
//		Seat seat = seatRepository.findById(seatIdx) .orElseThrow(() -> new ServiceException(ErrorCode.SEAT_NOT_FOUND));
//
//		// 티켓 생성 로직
//		Ticket ticket = new Ticket();
//		ticket.setUser(user);
//		ticket.changeplay(play);
//		ticket.setSeat(seat);
//		ticket.getSeat().setSeatStatus(SeatStatus.RESERVED);
//		// 티켓 저장 및 반환
//		return ticketRepository.save(ticket);
//	}
	
//	@Override
//	public boolean checkAvailability(play play) {
//		// 이벤트의 총 좌석 수
//		int totalSeats = ticketRepository.getTotalSeatsByplay(play);
//		// 이벤트의 예약된 좌석 수
//		int reservedSeats = ticketRepository.getReservedSeatsByplay(play);
//		// 잔여 좌석 수 계산 ( 전체 좌석 - 예약된 좌석 )
//		int availableSeats = totalSeats - reservedSeats;
//		// 잔여 좌석이 있으면 true 반환, 없으면 false 반환
//		return availableSeats > 0;
//	}

}