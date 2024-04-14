package com.skybory.seoulArt.domain.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDTO {

	private long id;
	private String title;
	private String detail;
	private String image;
}
