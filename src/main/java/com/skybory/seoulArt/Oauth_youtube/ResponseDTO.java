//package com.skybory.seoulArt.Oauth_youtube;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@AllArgsConstructor
//public class ResponseDTO {
//
//	private String code;
//	private String message;
//	
//	public ResponseDTO() {
//		this.code = ResponseCode.SUCCESS;
//		this.message = ResponseMessage.SUCCESS;
//	}
//	
//	// 디비 에러
//	public static ResponseEntity<ResponseDTO> databaseError() {
//		ResponseDTO responseBody = new ResponseDTO(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
//	}
//
//	// 유효성 에러
//	public static ResponseEntity<ResponseDTO> validationFail() {
//		ResponseDTO responseBody = new ResponseDTO(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL);
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
//	}
//}
