package com.skybory.seoulArt.domain.play.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePlayRequest {

	private String title;
	private String detail;
	private String image;
//	private MultipartFile imageFile;
}
