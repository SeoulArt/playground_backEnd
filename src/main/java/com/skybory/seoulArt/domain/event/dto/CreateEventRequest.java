package com.skybory.seoulArt.domain.event.dto;


import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEventRequest {

	private String title;
	private String detail;
	private String image;
	private MultipartFile imageFile;
}
