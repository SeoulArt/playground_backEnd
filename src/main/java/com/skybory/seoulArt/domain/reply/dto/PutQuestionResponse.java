package com.skybory.seoulArt.domain.reply.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutQuestionResponse {

	public PutQuestionResponse(Long qnaId) {
		this.qnaId = qnaId;
//		this.question = question;
	}

	private Long qnaId;
//	private String question;
}
