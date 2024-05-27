package com.skybory.seoulArt.domain.reply.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewListResponse {
	private Long reviewId;
	private String title;
	private String image;
}
