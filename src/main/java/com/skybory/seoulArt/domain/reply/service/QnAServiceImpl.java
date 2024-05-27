//package com.skybory.seoulArt.domain.reply.service;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.skybory.seoulArt.Oauth.JwtUtil;
//import com.skybory.seoulArt.domain.play.controller.PlayController;
//import com.skybory.seoulArt.domain.play.entity.Play;
//import com.skybory.seoulArt.domain.play.repository.PlayRepository;
//import com.skybory.seoulArt.domain.reply.dto.CreateReviewRequest;
//import com.skybory.seoulArt.domain.reply.dto.ReviewResponse;
//import com.skybory.seoulArt.domain.reply.entity.Review;
//import com.skybory.seoulArt.domain.reply.repository.ReviewRepository;
//import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceRequest;
//import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceResponse;
//import com.skybory.seoulArt.domain.user.dto.ImageRequest;
//import com.skybory.seoulArt.domain.user.dto.ImageResponse;
//import com.skybory.seoulArt.domain.user.dto.ReplyImageRequest;
//import com.skybory.seoulArt.domain.user.dto.UserDTO;
//import com.skybory.seoulArt.domain.user.entity.User;
//import com.skybory.seoulArt.domain.user.repository.UserRepository;
//import com.skybory.seoulArt.global.FileUploadService;
//import com.skybory.seoulArt.global.exception.ErrorCode;
//import com.skybory.seoulArt.global.exception.ServiceException;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//
//@Service
//@RequiredArgsConstructor
//@Log4j2
//@Transactional(readOnly = true)
//public class QuestionServiceImpl implements QnAService {
//
//	private final ReviewRepository reviewRepository;
//	private final UserRepository userRepository;
//	private final JwtUtil jwtUtil;
//	private final FileUploadService fileUploadService;
//	private final PlayRepository playRepository;
//
//	@Override
//	@Transactional
//	public ReviewResponse createReview(CreateReviewRequest request, HttpServletRequest requestServlet)
//			throws IOException {
//		log.info("리뷰 이미지 추가 메서드 실행");
//		User user = findUserByHeader(requestServlet);
//		Review review = new Review();
//		Play play = playRepository.findById(request.getPlayId()).orElseThrow(() -> new ServiceException(ErrorCode.PLAY_NOT_FOUND));
//		
//		String fileUrl;
//		ReviewResponse response = new ReviewResponse();
//		if (request.getImage() != null && !request.getImage().isEmpty()) {
//			// 파일 경로를 확인하여 URL 또는 실제 파일인지 판단 -> 값이 들어왔는지 판단
//			String filename = UUID.randomUUID().toString() + "_" + request.getImage().getOriginalFilename();
//			fileUrl = fileUploadService.uploadFile(request.getImage(), filename, "review");
//
//			review.setImage(fileUrl);
////			user.setCreator_image(fileUrl); // 새 이미지 URL을 유저 엔티티에 저장
//			response.setImage(fileUrl);
//
//			log.info("New image uploaded successfully: {}", fileUrl);
//
//		} else {
//			// 새 이미지가 제공되지 않은 경우, 기존 이미지 URL을 유지
//			review.setImage(null);
//			response.setImage(null);
//			log.info("이미지 삭제");
//			userRepository.save(user);
//		}
//		review.setUser(user);
//		review.setPlay(play);
//		review.changePlay(play);
//		review.changeUser(user);
//		review.setComment(request.getComment());
//		response.setComment(request.getComment());
//		response.setPlayId(request.getPlayId());
//		reviewRepository.save(review);
//		log.info("Image uploaded successfully");
//		return response;
//	}
//
//	
//	@Override
//	@Transactional
//	public ReviewResponse putReview(CreateReviewRequest request, HttpServletRequest requestServlet, Long reviewId)
//	        throws IOException {
//	    log.info("리뷰 수정 메서드 실행");
//
//	    // 리뷰 찾기
//	    Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ServiceException(ErrorCode.ENTITY_NOT_FOUND));
//	    User user = findUserByHeader(requestServlet);
//	    String role = user.getRole();
//
//	    // 리뷰 소유자 확인 및 역할에 따른 수정 권한 검증
//	    if (!role.equals("ROLE_ADMIN")) {
//	        if (!review.getUser().getId().equals(user.getId())) {
//	            throw new ServiceException(ErrorCode.HANDLE_ACCESS_DENIED);
//	        }
//	    }
//	    // 파일 처리 로직
//	    String fileUrl;
//	    if (request.getImage() != null && !request.getImage().isEmpty()) {
//	        String filename = UUID.randomUUID().toString() + "_" + request.getImage().getOriginalFilename();
//	        fileUrl = fileUploadService.uploadFile(request.getImage(), filename, "review");
//	        review.setImage(fileUrl);
//	        log.info("New image uploaded successfully: {}", fileUrl);
//	    } else {
//	        review.setImage(null); // 이미지 제거 시 null 설정
//	        log.info("이미지 삭제");
//	    }
//
//	    // 리뷰 데이터 업데이트
//	    review.setComment(request.getComment());
//	    review.setUser(user); // 사용자 설정
//	    review.setPlay(playRepository.findById(request.getPlayId()).orElseThrow(() -> new ServiceException(ErrorCode.PLAY_NOT_FOUND))); // 연극 설정
//
//	    reviewRepository.save(review); // 리뷰 저장
//
//	    // 응답 생성
//	    ReviewResponse response = new ReviewResponse();
//	    response.setComment(review.getComment());
//	    response.setImage(review.getImage());
//	    response.setPlayId(review.getPlay().getPlayId());
//
//	    log.info("리뷰 수정 완료");
//	    return response;
//	}
//
//	@Override
//	public List<ReviewResponse> showAll(Long playId) {
//
////		List<Review> replies = reviewRepository.findAll();
//		List<Review> replies = reviewRepository.findByPlayPlayId(playId);
//		// review -> reviewResponse 로 옮겨담기 ( Collectors 사용 )
//		List<ReviewResponse> responseList = replies.stream().map(review -> {
//			ReviewResponse response = new ReviewResponse();
//
//			// 매핑
//			response.setComment(review.getComment());
//			response.setImage(review.getImage());
//			response.setPlayId(playId);
//			return response;
//		}).collect(Collectors.toList());
//		return responseList;
//	}
//
//	@Override
//	public ReviewResponse showDetail(Long reviewId) {
//		Review review = reviewRepository.findById(reviewId)
//				.orElseThrow(() -> new ServiceException(ErrorCode.ENTITY_NOT_FOUND));
//		ReviewResponse response = new ReviewResponse();
//		
//		
//		response.setComment(review.getComment());
//		response.setImage(review.getImage());
//		response.setPlayId(review.getPlay().getPlayId());
//
//		return response;
//	}
//
//	@Override
//	@Transactional
//	public String deleteReview(Long reviewId, HttpServletRequest requestServlet) {
//		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ServiceException(ErrorCode.ENTITY_NOT_FOUND));
//		User user = findUserByHeader(requestServlet);
//		Long userId = user.getId();
//		String role = user.getRole();
//	    // 관리자 또는 에디터의 경우 사용자 ID가 일치하지 않아도 삭제 가능
//	    if (role.equals("ROLE_ADMIN") || role.equals("ROLE_EDITOR")) {
//	        try {
//	        	log.info("댓글강제삭제");
//	            reviewRepository.deleteById(reviewId);
//	            return "후기 삭제 성공"; // 삭제 성공
//	        } catch (Exception e) {
//	            return "후기 삭제 실패 : 권한 부족"; // 삭제 실패
//	        }
//	    }
//	 // 일반 사용자는 자신의 리뷰만 삭제 가능
//	    else if (role.equals("ROLE_USER")) {
//	        if (review.getUser().getId().equals(userId)) {
//	            try {
//	                reviewRepository.deleteById(reviewId);
//	                return "후기 삭제 성공"; // 삭제 성공
//	            } catch (Exception e) {
//	            	return "후기 삭제 실패 : 나의 리뷰가 아닙니다"; // 삭제 실패
//	            }
//	        } else {
//	            throw new ServiceException(ErrorCode.HANDLE_ACCESS_DENIED);
//	        }
//	    }
//
//	    // 그 외 경우 예외 처리
//	    throw new ServiceException(ErrorCode.HANDLE_ACCESS_DENIED);
//	}
//
//	public User findUserByHeader(HttpServletRequest request) {
//		String bearerToken = request.getHeader("Authorization");
//		String accessToken = bearerToken.substring(7);
//		Long userId = jwtUtil.getUserIdFromToken(accessToken);
//		User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
//		return user;
//	}
//
//
//}

package com.skybory.seoulArt.domain.reply.service;

import java.io.IOException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.skybory.seoulArt.Oauth.JwtUtil;
import com.skybory.seoulArt.domain.play.controller.PlayController;
import com.skybory.seoulArt.domain.play.entity.Play;
import com.skybory.seoulArt.domain.play.repository.PlayRepository;
import com.skybory.seoulArt.domain.reply.dto.AnswerRequest;
import com.skybory.seoulArt.domain.reply.dto.AnswerResponse;
import com.skybory.seoulArt.domain.reply.dto.CreateReviewRequest;
import com.skybory.seoulArt.domain.reply.dto.PostQuestionResponse;
import com.skybory.seoulArt.domain.reply.dto.PutAnswerRequest;
import com.skybory.seoulArt.domain.reply.dto.PutAnswerResponse;
import com.skybory.seoulArt.domain.reply.dto.PutQuestionRequest;
import com.skybory.seoulArt.domain.reply.dto.PutQuestionResponse;
import com.skybory.seoulArt.domain.reply.dto.QnAResponse;
import com.skybory.seoulArt.domain.reply.dto.QuestionRequest;
import com.skybory.seoulArt.domain.reply.dto.GetQuestionListResponse;
import com.skybory.seoulArt.domain.reply.dto.ReviewResponse;
import com.skybory.seoulArt.domain.reply.entity.QnA;
import com.skybory.seoulArt.domain.reply.entity.Review;
import com.skybory.seoulArt.domain.reply.repository.QnARepository;
import com.skybory.seoulArt.domain.reply.repository.ReviewRepository;
import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceRequest;
import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceResponse;
import com.skybory.seoulArt.domain.user.dto.ImageRequest;
import com.skybory.seoulArt.domain.user.dto.ImageResponse;
import com.skybory.seoulArt.domain.user.dto.UserDTO;
import com.skybory.seoulArt.domain.user.entity.User;
import com.skybory.seoulArt.domain.user.repository.UserRepository;
import com.skybory.seoulArt.global.FileUploadService;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class QnAServiceImpl implements QnAService {

	private final QnARepository qnaRepository;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final PlayRepository playRepository;

	@Override
	@Transactional
	public PostQuestionResponse postQuestion(QuestionRequest request, HttpServletRequest servletRequest, Long playId)
			throws IOException {

		// currentUser 찾기
		User user = findUserByHeader(servletRequest);
		Play play = playRepository.findById(playId).orElseThrow(() -> new ServiceException(ErrorCode.PLAY_NOT_FOUND));
		QnA qna = new QnA();

		if(user.getPlayList() == null) {
			throw new ServiceException(ErrorCode.HANDLE_ACCESS_DENIED);
		}
		
		// playList 문자열을 쉼표로 분할하고 리스트로 변환
	    List<String> userPlayIds = Arrays.asList(user.getPlayList().split(","));
		// 내 작품에는 질문 못해야해
	    if (userPlayIds.contains(String.valueOf(playId))) {
	        throw new ServiceException(ErrorCode.HANDLE_ACCESS_DENIED);
	    }
		
		qna.setUser(user);
		qna.setQuestion(request.getQuestion());
		qna.setPlay(play);
		qnaRepository.save(qna);

		PostQuestionResponse response = new PostQuestionResponse();

//		response.setUsername(user.getUsername());
//		response.setProfileImage(user.getProfileImage());
//		response.setQuestion(request.getComment());
		response.setQnaId(qna.getQnaId());

		return response;
	}

	@Override
	@Transactional
	public AnswerResponse postAnswer(AnswerRequest request, HttpServletRequest servletRequest, Long questionId)
			throws IOException {

		User user = findUserByHeader(servletRequest);
		QnA qna = qnaRepository.findById(questionId)
				.orElseThrow(() -> new ServiceException(ErrorCode.ENTITY_NOT_FOUND));

		if (!user.isEditor() && !user.getRole().equals("ROLE_ADMIN")) {
			throw new ServiceException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		// 엔티티 저장
		String answer = request.getAnswer();
		qna.setAnswer(answer);

		// response 생성
		AnswerResponse response = new AnswerResponse();
		response.setQnaId(questionId);
//		response.setAnswer(answer);
		return response;
	}

	@Override
	public QnAResponse getQnA(Long qnaId, HttpServletRequest request) {
		QnA qna = qnaRepository.findById(qnaId).orElseThrow(() -> new ServiceException(ErrorCode.QNA_NOT_FOUND));
		String question = qna.getQuestion();
		String answer = qna.getAnswer();
		User currentUser = findUserByHeader(request);
		Long playId = qna.getPlay().getPlayId();
		boolean isAuthor = qna.getUser().getId() == currentUser.getId();
		
		QnAResponse response = new QnAResponse();
		response.setQuestion(question);
		response.setAnswer(answer);
		response.setAuthor(isAuthor);
		response.setPlayId(playId);
		return response;
	}

	@Override
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

	@Override
	@Transactional
	public PutQuestionResponse putQuestion(PutQuestionRequest request, HttpServletRequest requestServlet,
			Long questionId) throws IOException {
		User user = findUserByHeader(requestServlet);
		String role = user.getRole();
		QnA qna = qnaRepository.findById(questionId).orElseThrow(() -> new ServiceException(ErrorCode.QNA_NOT_FOUND));

		if (!role.equals("ROLE_ADMIN")) {
			// admin 이 아니면 본인확인
			if (!qna.getUser().getId().equals(user.getId())) {
				throw new ServiceException(ErrorCode.HANDLE_ACCESS_DENIED);
			}
		}

		qna.setQuestion(request.getQuestion());
		qnaRepository.save(qna);
//		PutAnswerResponse response = new PutAnswerResponse(questionId, request.getQuestion());
		PutQuestionResponse response = new PutQuestionResponse(questionId);

		return response;
	}

	@Override
	@Transactional
	public PutAnswerResponse putAnswer(PutAnswerRequest request, HttpServletRequest requestServlet, Long questionId) {
		User user = findUserByHeader(requestServlet);
		QnA qna = qnaRepository.findById(questionId)
				.orElseThrow(() -> new ServiceException(ErrorCode.ENTITY_NOT_FOUND));

//		if (!user.isEditor() && !user.getRole().equals("ROLE_ADMIN")) {
//			throw new ServiceException(ErrorCode.HANDLE_ACCESS_DENIED);
//		}
		
	    // 조건 추가: ADMIN은 언제든지, Editor는 playList 값과 playId 값이 일치할 경우에만 수정 가능
	    if (!(user.getRole().equals("ROLE_ADMIN") ||
	          (user.isEditor() && user.getPlayList() != null && qna.getPlay().getPlayId() != null &&
	           user.getPlayList().equals(qna.getPlay().getPlayId().toString())))) {
	        throw new ServiceException(ErrorCode.HANDLE_ACCESS_DENIED);
	    }
		qna.setAnswer(request.getAnswer());
		qnaRepository.save(qna);

//		PutAnswerResponse response = new PutAnswerResponse(questionId, request.getAnswer());
		PutAnswerResponse response = new PutAnswerResponse(questionId);
		return response;
	}

	@Override
	@Transactional
	public String deleteQnA(Long qnaId, HttpServletRequest requestServlet) {
		User user = findUserByHeader(requestServlet);
		QnA qna = qnaRepository.findById(qnaId).orElseThrow(() -> new ServiceException(ErrorCode.ENTITY_NOT_FOUND));

		if (!qna.getUser().getId().equals(user.getId()) && !user.getRole().equals("ROLE_ADMIN")) {
			throw new ServiceException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		qnaRepository.delete(qna);
		return "QnA 삭제 성공";
	}

	@Override
	@Transactional
	public User findUserByHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		String accessToken = bearerToken.substring(7);
		Long userId = jwtUtil.getUserIdFromToken(accessToken);
		User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		return user;
	}

}
