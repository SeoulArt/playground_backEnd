package com.skybory.seoulArt.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyReviewListResponse {
	private Long reviewId;
	private String content;
	private String image;
}
