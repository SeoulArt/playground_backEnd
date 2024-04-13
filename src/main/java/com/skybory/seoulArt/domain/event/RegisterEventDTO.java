package com.skybory.seoulArt.domain.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterEventDTO {

	private String eventTitle;
	private String eventDetail;
	private String eventImage;
}
