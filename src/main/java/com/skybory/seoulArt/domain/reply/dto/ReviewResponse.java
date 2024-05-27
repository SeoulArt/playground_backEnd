package com.skybory.seoulArt.domain.reply.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponse {

//	private Long reviewId;
	private String title;
	private String content;
	private String image;
	private Long playId;	
}
