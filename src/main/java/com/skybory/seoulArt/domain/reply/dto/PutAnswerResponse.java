package com.skybory.seoulArt.domain.reply.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutAnswerResponse {

	public PutAnswerResponse(Long questionId, String comment) {
		this.questionId = questionId;
		this.comment = comment;
	}
	private Long questionId;
	private String comment;
}
