package com.skybory.seoulArt.domain.reply.service;

import java.util.List;
import java.util.Optional;

import com.skybory.seoulArt.domain.reply.dto.CreateReplyRequest;
import com.skybory.seoulArt.domain.reply.dto.ReplyResponse;

public interface ReplyService {

	ReplyResponse createReply(CreateReplyRequest replydto);

	boolean deleteReply(long replyIdx);

	List<ReplyResponse> showAll();

	Optional<ReplyResponse> showDetail(long replyIdx);
}
