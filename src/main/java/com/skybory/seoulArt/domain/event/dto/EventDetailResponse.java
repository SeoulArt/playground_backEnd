package com.skybory.seoulArt.domain.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDetailResponse {

	private long eventIdx;
	private String title;
	private String detail;
	private String image;
}
