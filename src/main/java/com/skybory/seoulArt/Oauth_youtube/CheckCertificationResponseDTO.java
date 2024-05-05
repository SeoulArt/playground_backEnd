//package com.skybory.seoulArt.Oauth_youtube;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import lombok.Getter;
//
//@Getter
//public class CheckCertificationResponseDTO {
//
//	private CheckCertificationResponseDTO() {
//		super();
//	}
//	
//	public static ResponseEntity<CheckCertificationResponseDTO> success() {
//		CheckCertificationResponseDTO responseBody = new CheckCertificationResponseDTO();
//		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
//	}
//
//	public static ResponseEntity<CheckCertificationResponseDTO> fail() {
//		CheckCertificationResponseDTO responseBody = new ResponseDTO();
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
//	}
//}
