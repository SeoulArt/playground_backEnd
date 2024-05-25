package com.skybory.seoulArt.domain.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skybory.seoulArt.domain.play.controller.PlayController;
import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceRequest;
import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceResponse;
import com.skybory.seoulArt.domain.user.dto.CreatorDetailResponse;
import com.skybory.seoulArt.domain.user.dto.CreatorListResponse;
import com.skybory.seoulArt.domain.user.dto.ImageRequest;
import com.skybory.seoulArt.domain.user.dto.ImageResponse;
import com.skybory.seoulArt.domain.user.dto.UserMobileRequest;
import com.skybory.seoulArt.domain.user.dto.UserMobileResponse;
import com.skybory.seoulArt.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.examples.Example;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
//@CrossOrigin
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;
	
	// 창작자 소개 (전체)
	@GetMapping("/creators")	// postman 완료(0417)
	@Operation(summary = "창작자 목록 조회", description = "모든 창작자를 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<List<CreatorListResponse>> showCreatorList(){
		log.info("창작자 불러오기");
		return ResponseEntity.ok(userService.showCreatorList());
	}
	
	// 창작자 소개2
	@GetMapping("/creator/{userId}")	// postman 완료(0417)
	@Operation(summary = "창작자 세부 조회", description = "해당 창작자의 세부사항을 조회합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러" )
	public ResponseEntity<CreatorDetailResponse> showCreatorDetail(@Parameter(description = "유저 id") @PathVariable Long userId){
		log.info("창작자 세부 조회");
		return ResponseEntity.ok(userService.showCreatorDetail(userId));
	}
	
//	@DeleteMapping("/{userId}")	// postman 완료 (0506)	//회원 탈퇴로 변경해서 authController . withdraw
//	@Operation(summary = "회원 탈퇴", description = "해당 유저를 삭제합니다")
//	@ApiResponse(responseCode="200", description="성공")
//	@ApiResponse(responseCode="400", description="에러")
//	public ResponseEntity<?> delete(@Parameter(description = "창작자 id") @PathVariable Long userId) {
//		userService.delete(userId);
//		return ResponseEntity.ok().build();
//	}
	
	@PostMapping(value = "/introduce", consumes = "multipart/form-data")
	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
	@Operation(summary = "창작자 소개 작성", description = "창작자 소개를 작성합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<CreatorIntroduceResponse> postIntroduction(@ModelAttribute CreatorIntroduceRequest request, HttpServletRequest requestServlet) throws IOException{
		return ResponseEntity.ok(userService.postIntroduction(request, requestServlet));
	}

	@PutMapping(value = "/introduce", consumes = "multipart/form-data")
	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
	@Operation(summary = "창작자 소개 수정", description = "창작자 소개를 수정합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<CreatorIntroduceResponse> putIntroduction(@ModelAttribute CreatorIntroduceRequest request, HttpServletRequest requestServlet) throws IOException{
		return ResponseEntity.ok(userService.postIntroduction(request, requestServlet));
	}

	
	@PostMapping("/mobile")
	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
	@Operation(summary = "핸드폰 번호 등록", description = "핸드폰 번호를 등록합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<UserMobileResponse> setMobile(@RequestBody UserMobileRequest request, HttpServletRequest requestServlet) {
		return ResponseEntity.ok(userService.setMobile(request, requestServlet));
	}
	
	@PutMapping(value = "/profileImage", consumes = "multipart/form-data")
	@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_CREATOR"})
	@Operation(summary = "프로필 이미지 수정", description = "프로필 이미지를 수정합니다")
	@ApiResponse(responseCode="200", description="성공")
	@ApiResponse(responseCode="400", description="에러")
	public ResponseEntity<ImageResponse> putProfileImage(@ModelAttribute ImageRequest request, HttpServletRequest requestServlet) throws IOException {
		return ResponseEntity.ok(userService.putProfileImage(request, requestServlet));
	}
	
}
