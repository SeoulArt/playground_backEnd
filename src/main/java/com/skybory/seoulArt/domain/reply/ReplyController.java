package com.skybory.seoulArt.domain.reply;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://reactserver")
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

	private final ReplyService replyService;

	@PostMapping("/create")
	public ResponseEntity<Reply> createReply(@RequestBody ReplyDTO replyDTO) {

		Reply reply = replyService.createReply(replyDTO);
		return ResponseEntity.ok(reply);
	}

	@DeleteMapping("/delete/{replyIdx}")
	public ResponseEntity<String> deleteReply(@PathVariable long replyIdx) {

		replyService.deleteReply(replyIdx);
		return ResponseEntity.ok("Reply successfully deleted");
	}

}
