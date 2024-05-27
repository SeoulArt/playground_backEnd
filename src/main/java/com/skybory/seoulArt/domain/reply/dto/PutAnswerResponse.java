package com.skybory.seoulArt.domain.reply.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutAnswerResponse {

	public PutAnswerResponse(Long qnaId) {
		this.qnaId = qnaId;
//		this.answer = answer;
	}
	private Long qnaId;
//	private String answer;
}
