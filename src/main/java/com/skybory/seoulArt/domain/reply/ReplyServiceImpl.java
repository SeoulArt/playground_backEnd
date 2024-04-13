package com.skybory.seoulArt.domain.reply;

import java.time.LocalDateTime;


import org.springframework.stereotype.Service;

import com.skybory.seoulArt.domain.user.User;
import com.skybory.seoulArt.global.SessionConst;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

	private final ReplyRepository replyRepository;
	private final HttpSession session;
 
	public Reply createReply(ReplyDTO replyDTO) {
		
		// 1. 유효성 검사?
		
		// 2. 매핑
		User user = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);
		Reply reply = new Reply();
		String comment = replyDTO.getReplyComment();
		reply.setReplyCommemt(comment);
		reply.setUser(user);
		reply.setReplyDateTime(LocalDateTime.now());
		// 3. 댓글 저장 및 반환
		return replyRepository.save(reply);
	}
	
	public void deleteReply(long replyIdx) {
        // 댓글을 삭제할 때 로그인한 사용자와 댓글을 작성한 사용자가 동일한 경우에만 삭제하도록 확인
//        Reply reply = replyRepository.findById(replyIdx).orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
//        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);
//
//        if (reply.getUser().equals(loginUser)) {
//            replyRepository.deleteById(replyIdx);
//        } else {
//            throw new IllegalStateException("해당 댓글을 삭제할 권한이 없습니다.");
//        }
		// 일단은 테스트용으로 누구나 삭제가능하게 바꿈
        replyRepository.deleteById(replyIdx);
	}
}
