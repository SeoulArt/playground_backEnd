package com.skybory.seoulArt.domain.reply.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionResponse {

public QuestionResponse(String userName, String profileImage, String comment) {
	this.userName = userName;
	this.profileImage = profileImage;
	this.comment = comment;
	}
	//	private Long userId;
	private String userName;
	private String profileImage;
	private String comment;
	
}
