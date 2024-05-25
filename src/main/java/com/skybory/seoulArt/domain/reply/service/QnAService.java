//package com.skybory.seoulArt.domain.reply.service;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//
//import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
//
//import com.skybory.seoulArt.domain.reply.dto.PostQuestionRequest;
//import com.skybory.seoulArt.domain.reply.dto.PostQuestionResponse;
//import com.skybory.seoulArt.domain.reply.dto.PutQuestionRequest;
//import com.skybory.seoulArt.domain.reply.dto.PutQuestionResponse;
//import com.skybory.seoulArt.domain.reply.dto.QuestionResponse;
//import com.skybory.seoulArt.domain.reply.dto.CreateReviewRequest;
//import com.skybory.seoulArt.domain.reply.dto.ReviewResponse;
//import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceRequest;
//import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceResponse;
//import com.skybory.seoulArt.domain.user.dto.ImageRequest;
//import com.skybory.seoulArt.domain.user.dto.ImageResponse;
//import com.skybory.seoulArt.domain.user.dto.ReplyImageRequest;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//
//public interface QuestionService {
//
//	PostQuestionResponse postQuestion(PostQuestionRequest request, HttpServletRequest servletRequest) throws IOException;
//
//
//	List<Question> getQuestionList(Long playId);
//
//	
//	String deleteQuestion(Long questionId, HttpServletRequest requestServlet);
//
//	List<QuestionResponse> showAll(Long playId);
//	
//	PutQuestionResponse putQuestion(PutQuestionRequest request, HttpServletRequest requestServlet, Long reviewId)
//			throws IOException;
//
//
//}
package com.skybory.seoulArt.domain.reply.service;

import java.io.IOException;
import java.util.List;
import com.skybory.seoulArt.domain.reply.dto.PutAnswerRequest;
import com.skybory.seoulArt.domain.reply.dto.PutAnswerResponse;
import com.skybory.seoulArt.domain.reply.dto.PutQuestionRequest;
import com.skybory.seoulArt.domain.reply.dto.PutQuestionResponse;
import com.skybory.seoulArt.domain.reply.dto.QnAResponse;
import com.skybory.seoulArt.domain.reply.dto.QuestionRequest;
import com.skybory.seoulArt.domain.reply.dto.QuestionResponse;
import com.skybory.seoulArt.domain.reply.dto.AnswerRequest;
import com.skybory.seoulArt.domain.reply.dto.AnswerResponse;
import com.skybory.seoulArt.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;


public interface QnAService {

	// POST
	QuestionResponse postQuestion(QuestionRequest request, HttpServletRequest servletRequest, Long playId) throws IOException;

	AnswerResponse postAnswer(AnswerRequest request,HttpServletRequest servletRequest, Long questionId) throws IOException;

	
	// GET
	QnAResponse getQnA(Long qnaId);
	//	AnswerResponse getAnswer(Long answerId);	// 이 부분은 getQnA 에 속해있음
	
	List<QuestionResponse> getQuestionList(Long playId);

	// PUT
	PutQuestionResponse putQuestion(PutQuestionRequest request, HttpServletRequest requestServlet, Long questionId)
			throws IOException;
	
	PutAnswerResponse putAnswer(PutAnswerRequest request, HttpServletRequest requestServlet, Long questionId);

	
	// DELETE
	String deleteQnA(Long qnaId, HttpServletRequest requestServlet);


	
	// auth
	User findUserByHeader(HttpServletRequest request);



}
