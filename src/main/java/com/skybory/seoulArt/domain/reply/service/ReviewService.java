package com.skybory.seoulArt.domain.reply.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.skybory.seoulArt.domain.reply.dto.CreateReviewRequest;
import com.skybory.seoulArt.domain.reply.dto.ReviewResponse;
import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceRequest;
import com.skybory.seoulArt.domain.user.dto.CreatorIntroduceResponse;
import com.skybory.seoulArt.domain.user.dto.ImageRequest;
import com.skybory.seoulArt.domain.user.dto.ImageResponse;
import com.skybory.seoulArt.domain.user.dto.ReplyImageRequest;

import jakarta.servlet.http.HttpServletRequest;


public interface ReviewService {

	ReviewResponse createReview(CreateReviewRequest request, HttpServletRequest servletRequest) throws IOException;


	List<ReviewResponse> showAll(Long playId);

	ReviewResponse showDetail(Long reviewId);


	String deleteReview(Long reviewId, HttpServletRequest requestServlet);




	ReviewResponse putReview(CreateReviewRequest request, HttpServletRequest requestServlet, Long reviewId)
			throws IOException;


}
