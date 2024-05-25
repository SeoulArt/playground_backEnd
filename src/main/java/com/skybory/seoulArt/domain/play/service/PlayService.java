package com.skybory.seoulArt.domain.play.service;

import java.util.List;

import com.skybory.seoulArt.domain.play.dto.CreatePlayRequest;
import com.skybory.seoulArt.domain.play.dto.CreatePlayResponse;
import com.skybory.seoulArt.domain.play.dto.PlayDetailResponse;
import com.skybory.seoulArt.domain.play.dto.EditPlayRequest;
import com.skybory.seoulArt.domain.play.entity.Play;


public interface PlayService {

	// 이벤트 생성
	CreatePlayResponse createPlay(CreatePlayRequest request);
	
	// 모든 이벤트 조회
	List<Play> getAllPlays();
	 
	// 이벤트 ID로 이벤트 조회
	Play getPlayById(Long playIdx);
	
	// 이벤트 삭제
	boolean deletePlayById(Long playIdx);

	PlayDetailResponse showDetail(Long playId);
	
	PlayDetailResponse editPlay(Long playId, EditPlayRequest request);
}
