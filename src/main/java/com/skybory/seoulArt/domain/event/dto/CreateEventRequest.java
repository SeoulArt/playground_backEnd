package com.skybory.seoulArt.domain.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEventRequest {

	private String eventTitle;
	private String eventDetail;
	private String eventImage;
}
