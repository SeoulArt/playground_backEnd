package com.skybory.seoulArt.domain.reply;

public interface ReplyService {
	public Reply createReply(ReplyDTO replydto);
	public void deleteReply(long replyIdx);
}
