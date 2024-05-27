package com.skybory.seoulArt.domain.reply.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutReviewRequest {

	private String content;
	private MultipartFile image;
	private String title;
}
