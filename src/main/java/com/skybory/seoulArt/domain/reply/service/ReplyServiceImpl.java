package com.skybory.seoulArt.domain.reply.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.skybory.seoulArt.domain.reply.dto.CreateReplyRequest;
import com.skybory.seoulArt.domain.reply.dto.ReplyResponse;
import com.skybory.seoulArt.domain.reply.entity.Reply;
import com.skybory.seoulArt.domain.reply.repository.ReplyRepository;
import com.skybory.seoulArt.domain.user.entity.User;
import com.skybory.seoulArt.global.SessionConst;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

	private final ReplyRepository replyRepository;
	private final HttpSession session;
 
	public ReplyResponse createReply(CreateReplyRequest request) {
		// 1. 유효성 검사?

		// 2. 매핑
		User user = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);
		Reply reply = new Reply();
		reply.setUser(user);
		reply.setReplyComment(request.getReplyComment());
		reply.setNickname(request.getNickname());
		reply.setReplyDateTime(LocalDateTime.now());
		
		// 3. 댓글 저장
		replyRepository.save(reply);
		
		// 4. dto 반환
		ReplyResponse response = new ReplyResponse();
		response.setNickname(request.getNickname());
		response.setReplyComment(request.getReplyComment());
		response.setReplyDateTime(request.getReplyDateTime());
		return response;
	}
	
	@Override
	public boolean deleteReply(long replyIdx) {
	    try {
	        replyRepository.deleteById(replyIdx);
	        return true; // 삭제 성공
	    } catch (Exception e) {
	        return false; // 삭제 실패
	    }
	}
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

	@Override
	public List<ReplyResponse> showAll() {
		 List<Reply> replies = replyRepository.findAll();

		 // Reply -> ReplyResponse 로 옮겨담기 ( Collectors 사용 )
		    List<ReplyResponse> responseList = replies.stream()
		    		.map(reply -> {
		                ReplyResponse response = new ReplyResponse();
		                
		                // 매핑
		                response.setNickname(reply.getNickname());
		                response.setReplyComment(reply.getReplyComment());
		                response.setReplyDateTime(reply.getReplyDateTime());
		                return response;
		            })
		            .collect(Collectors.toList());
		    return responseList;
	}

	@Override
	public Optional<ReplyResponse> showDetail(long replyId) {
		Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new ServiceException(ErrorCode.ENTITY_NOT_FOUND));
		ReplyResponse response = new ReplyResponse();
		
		response.setNickname(reply.getNickname());
		response.setReplyComment(reply.getReplyComment());
		response.setReplyDateTime(reply.getReplyDateTime());
		
		return Optional.of(response);
	}
 
}
