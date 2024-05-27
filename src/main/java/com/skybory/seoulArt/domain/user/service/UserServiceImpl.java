package com.skybory.seoulArt.domain.user.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.skybory.seoulArt.Oauth.JwtUtil;
import com.skybory.seoulArt.Oauth.dto.KakaoMemberResponse;
import com.skybory.seoulArt.Oauth.dto.NaverMemberResponse;
import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceRequest;
import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceResponse;
import com.skybory.seoulArt.domain.reply.dto.GetQuestionListResponse;
import com.skybory.seoulArt.domain.reply.dto.ReviewResponse;
import com.skybory.seoulArt.domain.reply.entity.QnA;
import com.skybory.seoulArt.domain.reply.entity.Review;
import com.skybory.seoulArt.domain.reply.repository.QnARepository;
import com.skybory.seoulArt.domain.reply.repository.ReviewRepository;
import com.skybory.seoulArt.domain.user.dto.CreatorDetailResponse;
import com.skybory.seoulArt.domain.user.dto.CreatorListResponse;
import com.skybory.seoulArt.domain.user.dto.ImageRequest;
import com.skybory.seoulArt.domain.user.dto.ImageResponse;
import com.skybory.seoulArt.domain.user.dto.MyAnswerListResponse;
import com.skybory.seoulArt.domain.user.dto.MyReviewListResponse;
import com.skybory.seoulArt.domain.user.dto.UserDTO;
import com.skybory.seoulArt.domain.user.dto.UserMobileRequest;
import com.skybory.seoulArt.domain.user.dto.UserMobileResponse;
import com.skybory.seoulArt.domain.user.entity.User;
import com.skybory.seoulArt.domain.user.repository.UserRepository;
import com.skybory.seoulArt.global.FileUploadService;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

//	@Autowired
	private final JwtUtil jwtUtil;

//	@Autowired
	private final UserRepository userRepository;

	private final QnARepository qnaRepository;
	private final ReviewRepository reviewRepository;
	
	private final FileUploadService fileUploadService;

//    public UserDTO getCurrentUser(HttpServletRequest request) {
////		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////	    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
////	    Long userId = userDTO.getUserId();  // UserDTO에 userId가 있다고 가정	   
////	    User user = findUserByCookie();
////	    User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
////        Long userId = jwtUtil.getUserIdFromToken(token);
//        return loadUserByUserId(userId);
//    }

	@Override
	public CreatorDetailResponse showCreatorDetail(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

		CreatorDetailResponse response = new CreatorDetailResponse();
		;
//		response.setId(user.getId());
		response.setProfileImage(user.getProfileImage());
		response.setDepartment(user.getDepartment());
		response.setDescription(user.getCreator_description());
		response.setImage(user.getCreator_image());
		response.setUsername(user.getUsername());
		response.setPlayList(user.getPlayList());
		return response;
	}

	@Override
	public List<CreatorListResponse> showCreatorList() {
		List<User> userList = userRepository.findAll();

		// 옮겨담기
		List<CreatorListResponse> responseList = userList.stream()
		// 부서가 없으면 검색되지 않음.
//	    		.filter(creator -> creator.getDepartment() != null)
				// ROLE_CREATOR 가 아니면 검색되지 않음
				.filter(creator -> creator.getRole().equals("ROLE_CREATOR") || creator.getRole().equals("ROLE_EDITOR"))

				.map(creator -> {
					CreatorListResponse response = new CreatorListResponse();

					// 매핑
					response.setId(creator.getId());
					response.setProfileImage(creator.getProfileImage());
					response.setDepartment(creator.getDepartment());
//	    			response.setDescription(creator.getCreator_description());
//	    			response.setImage(creator.getCreator_image());
					response.setUsername(creator.getUsername());
					response.setPlayList(creator.getPlayList());
					return response;
				}).collect(Collectors.toList());
		return responseList;
	}

	@Override
	@Transactional
	public void delete(Long userId) {
		userRepository.deleteById(userId);
	}

	@Override
	@Transactional
	public UserDTO register(KakaoMemberResponse kakaoMemberResponse) {
		// 회원 가입하자
		User user = new User();

		kakaoMemberResponse.getProperties().getNickname(); // '김태연' 가져옴
		user.setProfileImage(kakaoMemberResponse.getProperties().getProfile_image()); // 프사 : 링크 걸어줌
		user.setRole("ROLE_CREATOR"); // 왜인지 모르지만 롤이 설정되고있음..
		user.setUsername(kakaoMemberResponse.getProperties().getNickname()); // 테스트삼아 해보자 뭐가 후순위인지 궁금하네
		user.setVerifyCode("kakao " + kakaoMemberResponse.getId());
//		user.setPlayList(new ArrayList<>());
		userRepository.save(user);

		UserDTO response = new UserDTO();
		response.setProfileImage(user.getProfileImage());
		response.setRole(user.getRole());
		response.setUsername(user.getUsername());
		response.setUserId(user.getId());
		response.setPlayList(user.getPlayList());

		// 사용자의 티켓 검사해서 이벤트아이디 반환
		List<Map<String, Long>> ticketPlayPairs = user.getTicketList().stream().map(ticket -> {
			Map<String, Long> pair = new HashMap<>();
			pair.put("ticketId", ticket.getTicketIdx());
			pair.put("playId", ticket.getPlay().getPlayId());
			return pair;
		}).collect(Collectors.toList());

		response.setTicketPlayList(ticketPlayPairs);
		return response;
	}

	@Override
	@Transactional
	public UserDTO register(NaverMemberResponse naverMemberResponse) {
		// 회원 가입하자
		System.out.println("네이버 회원가입 시작");
		User user = new User();

		user.setProfileImage(naverMemberResponse.getNaverUserDetail().getProfile_image()); // 프사 : abc.jpg
		user.setRole("ROLE_CREATOR"); // ROLE_USER
		user.setUsername(naverMemberResponse.getNaverUserDetail().getName()); // username : 김태연
		user.setVerifyCode("naver " + naverMemberResponse.getNaverUserDetail().getId()); // verifycode : naver
																							// soaijogjfsi
		user.setPhoneNumber(naverMemberResponse.getNaverUserDetail().getMobile());
//		user.setPlayList();
		userRepository.save(user);

		UserDTO response = new UserDTO();
		response.setProfileImage(user.getProfileImage());
		response.setRole(user.getRole());
		response.setUsername(user.getUsername());
		response.setUserId(user.getId());
		response.setPlayList(user.getPlayList());
		// 사용자의 티켓 검사해서 이벤트아이디 반환
		List<Map<String, Long>> ticketPlayPairs = user.getTicketList().stream().map(ticket -> {
			Map<String, Long> pair = new HashMap<>();
			pair.put("ticketId", ticket.getTicketIdx());
			pair.put("playId", ticket.getPlay().getPlayId());
			return pair;
		}).collect(Collectors.toList());

		response.setTicketPlayList(ticketPlayPairs);
		return response;
	}

//	@Override
//	@Transactional
//	public UserDTO naverRegister(NaverMemberResponse naverMemberResponse) {
//		// 회원 가입하자
//		User user = new User();
//		
//		kakaoMemberResponse.getProperties().getNickname();	// '김태연' 가져옴
//		user.setProfileImage(kakaoMemberResponse.getProperties().getProfile_image());	// 프사 : 링크 걸어줌
//		user.setRole("ROLE_USER");	//왜인지 모르지만 롤이 설정되고있음..
//		user.setUsername(kakaoMemberResponse.getProperties().getNickname());	//테스트삼아 해보자 뭐가 후순위인지 궁금하네
//		user.setVerifyCode("kakao " + kakaoMemberResponse.getId());
//		userRepository.save(user);
//		
//		UserDTO response = new UserDTO();
//		response.setProfileImage(user.getProfileImage());
//		response.setRole(user.getRole());
//		response.setUsername(user.getUsername());
//		response.setUserId(user.getId());
//		return response;
//	}

	@Override
	@Transactional
	public UserDTO login(KakaoMemberResponse kakaoMemberResponse) {
		UserDTO response = new UserDTO();

		String verifyCode = "kakao " + kakaoMemberResponse.getId(); // 3432634754 -> 이 값을 반드시 저장해야함.
		User user = userRepository.findByVerifyCode(verifyCode);
		// 아이디 검색을 했는데 있을 경우..
		if (userRepository.findByVerifyCode(verifyCode) != null) {
			response.setProfileImage(user.getProfileImage());
			response.setRole(user.getRole());
			response.setUserId(user.getId());
			response.setUsername(user.getUsername());
			response.setPhoneNumber(user.getPhoneNumber());
			response.setDescription(user.getCreator_description());
			response.setPlayList(user.getPlayList());
			response.setEditor(user.isEditor());
			// 사용자의 티켓 검사해서 이벤트아이디 반환
			List<Map<String, Long>> ticketPlayPairs = user.getTicketList().stream().map(ticket -> {
				Map<String, Long> pair = new HashMap<>();
				pair.put("ticketId", ticket.getTicketIdx());
				pair.put("playId", ticket.getPlay().getPlayId());
				return pair;
			}).collect(Collectors.toList());

			response.setTicketPlayList(ticketPlayPairs);

//			response.setPhoneNumber(user.getPhoneNumber());
			System.out.println("카카오 로그인 : 나 아이디 검색했는데 있더라");
			// 그녀석 그대로 가져와.
		} else {
			// 아이디 검색을 했는데 없을 경우 -> 회원가입
			response = register(kakaoMemberResponse);

		}
		return response;
	}

	@Override
	@Transactional
	public UserDTO login(NaverMemberResponse naverMemberResponse) {
		UserDTO response = new UserDTO();

		String verifyCode = "naver " + naverMemberResponse.getNaverUserDetail().getId(); // 3432634754 -> 이 값을 반드시
																							// 저장해야함.
		User user = userRepository.findByVerifyCode(verifyCode);
		// 아이디 검색을 했는데 있을 경우..
		if (userRepository.findByVerifyCode(verifyCode) != null) {
			response.setProfileImage(user.getProfileImage());
			response.setRole(user.getRole());
			response.setUserId(user.getId());
			response.setUsername(user.getUsername());
			response.setPhoneNumber(user.getPhoneNumber());
			response.setDescription(user.getCreator_description());
			response.setPlayList(user.getPlayList());
			response.setEditor(user.isEditor());
			// 사용자의 티켓 검사해서 이벤트아이디 반환
			List<Map<String, Long>> ticketPlayPairs = user.getTicketList().stream().map(ticket -> {
				Map<String, Long> pair = new HashMap<>();
				pair.put("ticketId", ticket.getTicketIdx());
				pair.put("playId", ticket.getPlay().getPlayId());
				return pair;
			}).collect(Collectors.toList());

			response.setTicketPlayList(ticketPlayPairs);

//			response.setPhoneNumber(user.getPhoneNumber());
			System.out.println("네이버 로그인 : 나 아이디 검색했는데 있더라");
			// 그녀석 그대로 가져와.
		} else {
			// 아이디 검색을 했는데 없을 경우 -> 회원가입
			response = register(naverMemberResponse);

		}
		return response;
	}

	@Override
	public UserDTO loadUserByUserId(Long userId) throws UsernameNotFoundException {
		User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(user.getId());
		userDTO.setUsername(user.getUsername());
		userDTO.setRole(user.getRole());
		userDTO.setProfileImage(user.getProfileImage());
		userDTO.setPhoneNumber(user.getPhoneNumber());
		userDTO.setDescription(user.getCreator_description());
		userDTO.setPlayList(user.getPlayList());
		userDTO.setEditor(user.isEditor());
		// 사용자의 티켓 검사해서 이벤트아이디 반환
		// 사용자의 티켓 검사해서 티켓 ID와 이벤트 ID의 쌍을 반환
		List<Map<String, Long>> ticketPlayPairs = user.getTicketList().stream().map(ticket -> {
			Map<String, Long> pair = new HashMap<>();
			pair.put("ticketId", ticket.getTicketIdx());
			pair.put("playId", ticket.getPlay().getPlayId());
			return pair;
		}).collect(Collectors.toList());

		userDTO.setTicketPlayList(ticketPlayPairs);
//        userDTO.setPlayIds(playIds);
		log.info("UserDTO returned from loadUserByUserId: " + userDTO);
		return userDTO;
	}

	@Override
	@Transactional
	public CreatorIntroduceResponse postIntroduction(CreatorIntroduceRequest request, HttpServletRequest requestServlet)
			throws IOException {
		log.info("창작자 소개 작성 메서드 실행");
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
//	    Long userId = userDTO.getUserId();  // UserDTO에 userId가 있다고 가정	    
//	    User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

//	    User user = findUserByCookie();
		User user = findUserByHeader(requestServlet);
		String fileUrl;
		CreatorIntroduceResponse response = new CreatorIntroduceResponse();
//		String fileUrl = user.getCreator_image(); // 기존에 저장된 이미지 URL을 불러옴
		if (request.getImage() != null && !request.getImage().isEmpty()) {
			// 파일 경로를 확인하여 URL 또는 실제 파일인지 판단 -> 값이 들어왔는지 판단
//		       // 새로운 실제 파일을 처리
			String filename = UUID.randomUUID().toString() + "_" + request.getImage().getOriginalFilename();
			fileUrl = fileUploadService.uploadFile(request.getImage(), filename, "creator");
			user.setCreator_image(fileUrl); // 새 이미지 URL을 유저 엔티티에 저장
			response.setImage(fileUrl);
			log.info("New image uploaded successfully: {}", fileUrl);

		} else {
			// 새 이미지가 제공되지 않은 경우, 기존 이미지 URL을 유지
			user.setCreator_image(null);
			response.setImage(null);
//			user.setCreator_image(null); // Set the image URL to null
			log.info("이미지 삭제");
			userRepository.save(user);
		}

//		if (request.getImage().getOriginalFilename().startsWith("/creator")) {
//			// 이미지 경로가 "/creator"로 시작하면, 이는 이미 저장된 URL
//			log.info("Provided file is a URL, retaining existing image");
//			user.setCreator_image(fileUrl);
//		} else {
//			// "/creator"로 시작하지 않으면, 이는 새로운 실제 파일
//			String filename = UUID.randomUUID().toString() + "_" + request.getImage().getOriginalFilename();
//			fileUrl = fileUploadService.uploadFile(request.getImage(), filename);
//			user.setCreator_image(fileUrl); // 유저 엔티티에 새 이미지 URL 저장
//			log.info("New image uploaded successfully: {}", fileUrl);
//		}

//	    String filename = UUID.randomUUID().toString() + "_" + request.getImage().getOriginalFilename();
//	    String fileUrl = fileUploadService.uploadFile(request.getImage(), filename);

		// 유저 엔티티에 정보 추가
		user.setCreator_description(request.getDescription());
//		user.setCreator_image(fileUrl);
//	    user.setDepartment(request.getDepartment());

//		CreatorIntroduceResponse response = new CreatorIntroduceResponse();
//	    response.setDepartment(request.getDepartment());
		response.setDescription(request.getDescription());
//		response.setImage(fileUrl);
//	    response.setProfileImage(user.getProfileImage());
//	    response.setUsername(user.getUsername());

		log.info("Image uploaded successfully");
		return response;
	}

	@Override
	@Transactional
	public UserMobileResponse setMobile(UserMobileRequest request, HttpServletRequest requestServlet) {
		String mobile = request.getMobile();
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
//	    Long userId = userDTO.getUserId();  // UserDTO에 userId가 있다고 가정	    
//	    User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
//		User user = findUserByCookie();
		User user = findUserByHeader(requestServlet);

		user.setPhoneNumber(mobile);
		UserMobileResponse response = new UserMobileResponse();
		response.setMobile(mobile);

		return response;
	}

//	@Override
//	public User findUserByCookie() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UserDTO userDTO = (UserDTO) authentication.getPrincipal();
//	    Long userId = userDTO.getUserId();  // UserDTO에 userId가 있다고 가정	    
//	    User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
//	    return user;
//	}

	@Override
	public User findUserByHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		String accessToken = bearerToken.substring(7);
		Long userId = jwtUtil.getUserIdFromToken(accessToken);
		User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		return user;
	}

	@Override
	@Transactional
	public ImageResponse putProfileImage(ImageRequest request, HttpServletRequest requestServlet)
			throws IOException {
		User user = findUserByHeader(requestServlet);
		String fileUrl;
		ImageResponse response = new ImageResponse();
//		String fileUrl = user.getCreator_image(); // 기존에 저장된 이미지 URL을 불러옴
		if (request.getImage() != null && !request.getImage().isEmpty()) {
			// 파일 경로를 확인하여 URL 또는 실제 파일인지 판단 -> 값이 들어왔는지 판단
//		       // 새로운 실제 파일을 처리
			String filename = UUID.randomUUID().toString() + "_" + request.getImage().getOriginalFilename();
			fileUrl = fileUploadService.uploadFile(request.getImage(), filename, "profileImage");
			user.setProfileImage(fileUrl); // 새 이미지 URL을 유저 엔티티에 저장
			response.setImage(fileUrl);
			log.info("New image uploaded successfully: {}", fileUrl);

		} else {
			// 새 이미지가 제공되지 않은 경우, 기존 이미지 URL을 유지
			throw new IllegalArgumentException("프로필 이미지는 필수입니다. 이미지를 업로드해주세요.");
		}
		return response;

	}

	@Override
	public List<GetQuestionListResponse> getMyQuestionList(HttpServletRequest requestServlet) {
		User user = findUserByHeader(requestServlet);
		List<QnA> qnas = qnaRepository.findQnasByUser_Id(user.getId());
		
	    List<GetQuestionListResponse> response = qnas.stream()
	            .map(qna -> {
	                boolean isAnswered = qna.getAnswer() != null; // 답변 여부 확인
	                return new GetQuestionListResponse(
	                		qna.getQnaId(),
	                    qna.getQuestion(),
	                    isAnswered
	                );
	            }).collect(Collectors.toList());

	        return response;
	}

	@Override
	public List<MyReviewListResponse> getMyReviewList(HttpServletRequest requestServlet) {
		User user = findUserByHeader(requestServlet);
		List<Review> reviews = reviewRepository.findByUser_Id(user.getId());
		
		
		List<MyReviewListResponse> responseList = reviews.stream().map(review -> {
			MyReviewListResponse response = new MyReviewListResponse();

			// 매핑
			response.setReviewId(review.getReviewId());
			response.setContent(review.getContent());
			response.setImage(review.getImage());
			return response;
		}).collect(Collectors.toList());
		return responseList;
	}

	@Override
	public List<MyAnswerListResponse> getMyAnswerList(HttpServletRequest requestServlet) {
		User user = findUserByHeader(requestServlet);
	
		// admin 이면 전부 다 보이고, isEditor 가 true 이면 자기꺼만 보이게 할까? 응 그러자.
		if (user.getRole() == "ROLE_ADMIN") {
			List<QnA> qnas = qnaRepository.findQnasByUser_Id(user.getId());
			List<MyAnswerListResponse> response = qnas.stream()
		            .map(qna -> 
		                new MyAnswerListResponse(
		                		qna.getQnaId(),
		                    qna.getQuestion()
		                )
		            ).collect(Collectors.toList());
			return response;
		}
		// 에디터라면
		else if (user.isEditor()) {
			Long playId = Long.parseLong(user.getPlayList());
			List<QnA> qnas = qnaRepository.findByPlayPlayId(playId);
			
			List<MyAnswerListResponse> response = qnas.stream()
		            .map(qna -> 
		                new MyAnswerListResponse(
		                		qna.getQnaId(),
		                    qna.getAnswer()
		                )
		            ).collect(Collectors.toList());
			return response;
		}
		
		else {
			throw new ServiceException(ErrorCode.HANDLE_ACCESS_DENIED);
		}
	}
	
	public List<GetQuestionListResponse> getQuestionList(Long playId) {
		List<QnA> qnas = qnaRepository.findByPlayPlayId(playId);
	    List<GetQuestionListResponse> response = qnas.stream()
	            .map(qna -> {
	                boolean isAnswered = qna.getAnswer() != null; // 답변 여부 확인
	                return new GetQuestionListResponse(
	                		qna.getQnaId(),
	                    qna.getQuestion(),
	                    isAnswered
	                );
	            }).collect(Collectors.toList());

	        return response;
	}
}
