//package com.skybory.seoulArt.domain.reply.repository;
//
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.skybory.seoulArt.domain.reply.entity.QnA;
//
//
//public interface QuestionRepository extends JpaRepository<QnA, Long> {
//}
package com.skybory.seoulArt.domain.reply.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skybory.seoulArt.domain.reply.entity.QnA;


public interface QnARepository extends JpaRepository<QnA, Long> {

	List<QnA> findByPlayPlayId(Long playId);

	List<QnA> findQnasByUser_Id(Long id);
	
}

