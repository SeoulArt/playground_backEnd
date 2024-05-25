package com.skybory.seoulArt.domain.reply.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequest {
	
	private Long playId;
	private String comment;
	private MultipartFile image;
}
