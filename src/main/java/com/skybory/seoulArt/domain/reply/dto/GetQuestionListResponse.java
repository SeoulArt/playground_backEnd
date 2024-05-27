package com.skybory.seoulArt.domain.reply.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetQuestionListResponse {

public GetQuestionListResponse(Long qnaId, String question, boolean isAnswered) {
	this.qnaId = qnaId;
//	this.username = username;
//	this.profileImage = profileImage;
	this.question = question;
	this.isAnswered = isAnswered;
	}
	private Long qnaId;
//	private String username;
//	private String profileImage;
	private String question;
	private boolean isAnswered;
	
}
