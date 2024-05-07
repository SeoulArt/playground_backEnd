package com.skybory.seoulArt.domain.reply.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.skybory.seoulArt.domain.reply.dto.CreateReplyRequest;
import com.skybory.seoulArt.domain.reply.dto.ReplyResponse;
import com.skybory.seoulArt.domain.reply.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
//@CrossOrigin(origins = "http://reactserver")
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

	private final ReplyService replyService;

	@GetMapping("/main")
	@Operation(summary = "댓글 목록 조회", description = "모든 댓글 목록을 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<List<ReplyResponse>> viewReplyAll() {
		return ResponseEntity.ok(replyService.showAll());
	}

	
	@PostMapping("/create")
	@Operation(summary = "댓글 생성", description = "댓글을 생성합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<ReplyResponse> createReply(@RequestBody CreateReplyRequest request) {
		return ResponseEntity.ok(replyService.createReply(request));
	}
	
	// of 메서드는 어떤걸까..
	@GetMapping("/detail/{replyIdx}")
	@Operation(summary = "댓글 상세정보 조회", description = "댓글의 상세정보를 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<ReplyResponse> viewReplyDetail(@Parameter(description = "댓글 id") @PathVariable Long replyIdx) {
		return ResponseEntity.of(replyService.showDetail(replyIdx));
	}
	
	@DeleteMapping("/delete/{replyIdx}")
	@Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<Boolean> deleteReply(@Parameter(description = "댓글 id") @PathVariable Long replyIdx) {
		return ResponseEntity.ok(replyService.deleteReply(replyIdx));
	}

}
