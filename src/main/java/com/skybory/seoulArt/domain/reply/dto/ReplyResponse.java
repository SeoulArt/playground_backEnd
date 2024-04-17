package com.skybory.seoulArt.domain.reply.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyResponse {

	private String nickname;
	private String replyComment;
	private LocalDateTime replyDateTime;
}
