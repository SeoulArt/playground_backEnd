package com.skybory.seoulArt.domain.reply.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skybory.seoulArt.domain.reply.dto.CreateReplyRequest;
import com.skybory.seoulArt.domain.reply.dto.ReplyResponse;
import com.skybory.seoulArt.domain.reply.service.ReplyService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://reactserver")
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

	private final ReplyService replyService;

	@GetMapping("main")
	public ResponseEntity<List<ReplyResponse>> viewReplyAll() {
		return ResponseEntity.ok(replyService.showAll());
	}

	@PostMapping("/create")
	public ResponseEntity<ReplyResponse> createReply(@RequestBody CreateReplyRequest request) {
		return ResponseEntity.ok(replyService.createReply(request));
	}
	
	// of 메서드는 어떤걸까..
	@GetMapping("/detail/{replyIdx}")
	public ResponseEntity<ReplyResponse> viewReplyDetail(@PathVariable long replyIdx) {
		return ResponseEntity.of(replyService.showDetail(replyIdx));
	}
	
	@DeleteMapping("/delete/{replyIdx}")
	public ResponseEntity<Boolean> deleteReply(@PathVariable long replyIdx) {
//		replyService.deleteReply(replyIdx);
		return ResponseEntity.ok(replyService.deleteReply(replyIdx));
	}

}
