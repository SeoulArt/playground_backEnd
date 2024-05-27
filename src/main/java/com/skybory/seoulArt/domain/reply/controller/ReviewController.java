package com.skybory.seoulArt.domain.reply.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skybory.seoulArt.domain.reply.dto.CreateReviewRequest;
import com.skybory.seoulArt.domain.reply.dto.CreateReviewResponse;
import com.skybory.seoulArt.domain.reply.dto.PutReviewRequest;
import com.skybory.seoulArt.domain.reply.dto.PutReviewResponse;
import com.skybory.seoulArt.domain.reply.dto.ReviewListResponse;
import com.skybory.seoulArt.domain.reply.dto.ReviewResponse;
import com.skybory.seoulArt.domain.reply.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
//@CrossOrigin(origins = "http://reactserver")
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

	private final ReviewService reviewService;

//	@PostMapping(value = "",  consumes = "multipart/form-data")	// 완성
	@PostMapping("")	// 완성
	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
	@Operation(summary = "후기 생성", description = "후기를 생성합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<CreateReviewResponse> createReview(@ModelAttribute CreateReviewRequest request, HttpServletRequest servletRequest) throws IOException {
		return ResponseEntity.ok(reviewService.createReview(request, servletRequest));
	}
	
	@PutMapping("/{reviewId}")	// 완성
	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
	@Operation(summary = "후기 수정", description = "후기를 수정합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<PutReviewResponse> putReview(@ModelAttribute PutReviewRequest request, HttpServletRequest servletRequest, @PathVariable("reviewId") Long reviewId) throws IOException {
		return ResponseEntity.ok(reviewService.putReview(request, servletRequest, reviewId));
	}
	
	
	@GetMapping("/list/{playId}")
	@Operation(summary = "후기 목록 조회", description = "playId 로 검색해서 해당 공연의 모든 후기 목록을 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<List<ReviewListResponse>> getReviewList(@PathVariable("playId") Long playId) {
		log.info("후기 목록을 조회합니다");
		return ResponseEntity.ok(reviewService.showAll(playId));
	}
	

	@GetMapping("/{reviewId}")
	@Operation(summary = "후기 상세정보 조회", description = "후기 상세정보를 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<ReviewResponse> getReviewDetail(@Parameter(description = "후기 id") @PathVariable("reviewId") Long reviewId) {
		return ResponseEntity.ok(reviewService.showDetail(reviewId));
	}
	

	
	@DeleteMapping("/{reviewId}")
	@Secured({"ROLE_USER", "ROLE_ADMIN" , "ROLE_CREATOR"})
	@Operation(summary = "후기 삭제", description = "후기를 삭제합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<String> deleteReview(@Parameter(description = "댓글 id") @PathVariable("reviewId") Long reviewId, HttpServletRequest requestServlet) {
		return ResponseEntity.ok(reviewService.deleteReview(reviewId, requestServlet));
	}

}
