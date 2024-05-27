package com.skybory.seoulArt.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyAnswerListResponse {

	public MyAnswerListResponse(Long qnaId, String question) {
		this.qnaId = qnaId;
		this.question = question;
	}
	private Long qnaId;
	private String question;
}
