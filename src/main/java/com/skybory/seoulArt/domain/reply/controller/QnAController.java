package com.skybory.seoulArt.domain.reply.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.skybory.seoulArt.domain.reply.dto.PutAnswerRequest;
import com.skybory.seoulArt.domain.reply.dto.PutAnswerResponse;
import com.skybory.seoulArt.domain.reply.dto.PutQuestionRequest;
import com.skybory.seoulArt.domain.reply.dto.PutQuestionResponse;
import com.skybory.seoulArt.domain.reply.dto.QnAResponse;
import com.skybory.seoulArt.domain.reply.dto.QuestionRequest;
import com.skybory.seoulArt.domain.reply.dto.GetQuestionListResponse;
import com.skybory.seoulArt.domain.reply.dto.AnswerRequest;
import com.skybory.seoulArt.domain.reply.dto.AnswerResponse;
import com.skybory.seoulArt.domain.reply.dto.PostQuestionResponse;
import com.skybory.seoulArt.domain.reply.service.QnAService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnAController {

	private final QnAService qnaService;

	@PostMapping("/question/{playId}")	// 완성
	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
	@Operation(summary = "질문 생성", description = "질문을 생성합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<PostQuestionResponse> postQuestion(@RequestBody QuestionRequest request, HttpServletRequest servletRequest, @PathVariable("playId") Long playId) throws IOException {
		return ResponseEntity.ok(qnaService.postQuestion(request, servletRequest, playId));
	}

	@PostMapping("/answer/{qnaId}")	// 완성
	@Secured({"ROLE_ADMIN", "ROLE_CREATOR"})
	@Operation(summary = "답변 생성", description = "답변을 생성합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<AnswerResponse> postAnswer(@RequestBody AnswerRequest request, HttpServletRequest servletRequest, @PathVariable("qnaId") Long qnaId) throws IOException {
		return ResponseEntity.ok(qnaService.postAnswer(request, servletRequest, qnaId));
	}
	
	
	@GetMapping("/{qnaId}")
	@Operation(summary = "질문 상세정보 조회", description = "질문 상세정보를 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<QnAResponse> getQuestionDetail(@Parameter(description = "후기 id") @PathVariable("qnaId") Long qnaId, HttpServletRequest request) {
		return ResponseEntity.ok(qnaService.getQnA(qnaId, request));
	}
	
	
	@GetMapping("/list/{playId}")
	@Operation(summary = "질문 목록 조회", description = "playId 로 검색해서 해당 공연의 모든 질문 목록을 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<List<GetQuestionListResponse>> getQuestionList(@PathVariable("playId") Long playId) {
		log.info("후기 목록을 조회합니다");
		return ResponseEntity.ok(qnaService.getQuestionList(playId));
	}
	
	
	@PutMapping("/question/{qnaId}")	// 완성
	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
	@Operation(summary = "질문 수정", description = "질문을 수정합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<PutQuestionResponse> putQuestion(@RequestBody PutQuestionRequest request, HttpServletRequest servletRequest, @PathVariable("qnaId") Long qnaId) throws IOException {
		return ResponseEntity.ok(qnaService.putQuestion(request, servletRequest, qnaId));
	}
	
	@PutMapping("/answer/{qnaId}")	// 완성
	@Secured({"ROLE_USER", "ROLE_CREATOR", "ROLE_ADMIN"})
	@Operation(summary = "답변 수정", description = "답변을 수정합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<PutAnswerResponse> putAnswer(@RequestBody PutAnswerRequest request, HttpServletRequest servletRequest, @PathVariable("qnaId") Long qnaId) throws IOException {
		return ResponseEntity.ok(qnaService.putAnswer(request, servletRequest, qnaId));
	}
	
	@DeleteMapping("/{qnaId}")
	@Secured({"ROLE_USER", "ROLE_ADMIN" , "ROLE_CREATOR"})
	@Operation(summary = "질문 삭제", description = "질문을 삭제합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<String> deleteQuestion(@Parameter(description = "댓글 id") @PathVariable("qnaId") Long qnaId, HttpServletRequest requestServlet) {
		return ResponseEntity.ok(qnaService.deleteQnA(qnaId, requestServlet));
	}
	


}
