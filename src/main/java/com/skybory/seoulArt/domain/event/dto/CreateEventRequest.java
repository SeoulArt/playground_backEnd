package com.skybory.seoulArt.domain.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEventRequest {

	private String title;
	private String detail;
	private String image;
}
