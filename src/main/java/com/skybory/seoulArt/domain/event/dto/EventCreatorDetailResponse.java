package com.skybory.seoulArt.domain.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventCreatorDetailResponse {

	long id;
	String name;
	String department;
	String image;
	String description;
}
