package com.skybory.seoulArt.domain.reply.service;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

import com.skybory.seoulArt.domain.reply.dto.CreateReviewRequest;
import com.skybory.seoulArt.domain.reply.dto.CreateReviewResponse;
import com.skybory.seoulArt.domain.reply.dto.PutReviewRequest;
import com.skybory.seoulArt.domain.reply.dto.PutReviewResponse;
import com.skybory.seoulArt.domain.reply.dto.ReviewListResponse;
import com.skybory.seoulArt.domain.reply.dto.ReviewResponse;
import jakarta.servlet.http.HttpServletRequest;


public interface ReviewService {

	CreateReviewResponse createReview(CreateReviewRequest request, HttpServletRequest servletRequest) throws IOException;


	List<ReviewListResponse> showAll(Long playId);

	ReviewResponse showDetail(Long reviewId);


	String deleteReview(Long reviewId, HttpServletRequest requestServlet);



	PutReviewResponse putReview(PutReviewRequest request, HttpServletRequest requestServlet, Long reviewId)
			throws IOException;


}
