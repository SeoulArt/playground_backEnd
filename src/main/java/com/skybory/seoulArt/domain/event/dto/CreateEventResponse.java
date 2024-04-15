package com.skybory.seoulArt.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateEventResponse {

	private String eventTitle;
	private String eventDetail;
	private String eventImage;
}
