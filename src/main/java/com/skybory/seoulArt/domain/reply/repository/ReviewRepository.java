package com.skybory.seoulArt.domain.reply.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skybory.seoulArt.domain.reply.entity.Review;


public interface ReviewRepository extends JpaRepository<Review, Long> {
	 List<Review> findByPlayPlayId(Long playId);
}
