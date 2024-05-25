package com.skybory.seoulArt.domain.reply.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutAnswerRequest {

	private Long questionId;
	private String comment;
}
