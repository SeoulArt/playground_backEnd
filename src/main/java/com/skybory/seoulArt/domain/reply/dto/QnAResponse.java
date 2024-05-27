package com.skybory.seoulArt.domain.reply.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnAResponse {

	private String question;
	private String answer;
	private boolean isAuthor;
	private Long playId;
}
