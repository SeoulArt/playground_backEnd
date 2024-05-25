package com.skybory.seoulArt.domain.play.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayDetailResponse {

	private Long playId;
	private String title;
	private String detail;
	private String image;
}
