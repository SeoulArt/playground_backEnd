package com.skybory.seoulArt.domain.reply.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponse {

	private String comment;
//	private LocalDateTime dateTime;
	private String image;
	private Long playId;
}
