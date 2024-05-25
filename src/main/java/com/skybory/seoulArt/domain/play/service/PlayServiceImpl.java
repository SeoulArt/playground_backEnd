package com.skybory.seoulArt.domain.play.service;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.skybory.seoulArt.domain.play.dto.CreatePlayRequest;
import com.skybory.seoulArt.domain.play.dto.CreatePlayResponse;
import com.skybory.seoulArt.domain.play.dto.PlayDetailResponse;
import com.skybory.seoulArt.domain.play.dto.EditPlayRequest;
import com.skybory.seoulArt.domain.play.entity.Play;
import com.skybory.seoulArt.domain.play.repository.PlayRepository;
import com.skybory.seoulArt.global.exception.ErrorCode;
import com.skybory.seoulArt.global.exception.ServiceException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

// 트랜잭션 리드온리 추가해야함
@Service
@Transactional(readOnly = true)
@Log4j2
@RequiredArgsConstructor
public class PlayServiceImpl implements PlayService {

	private final PlayRepository playRepository;
//	private final FileUploadService fileUploadService;  // FileUploadService 주입

//	@Override
//	@Transactional
//	public CreateplayResponse createplay(CreateplayRequest request) {
//
//		// 1. validation 체크 해야할까?
////    	validateplayDTO(request);
//
////        // 2. 새로운 이벤트 생성 및 매핑
//		play play = mapJoinDTOToUser(request);
//
//		// 3. DB에 이벤트 저장
//		playRepository.save(play);
//
//		// 4. response dto 생성 및 반환
//		CreateplayResponse response = new CreateplayResponse();
//
//		// 5. dto에 값 미팽
//		response.setplayDetail(request.getDetail());
//		response.setplayImage(request.getImage());
//		response.setplayTitle(request.getTitle());
//
//		// 6. 응답 반환
//		return response;
//	}
	
	@Override
	@Transactional
	public CreatePlayResponse createPlay(CreatePlayRequest request) {
		try {
			validatePlayDTO(request);
			Play play = mapJoinDTOToUser(request);
			playRepository.save(play);
		}
			
			catch (DataIntegrityViolationException e) {
			    log.error("Data integrity violation on creating play: {}", e.getMessage());
			    throw new ServiceException(ErrorCode.INVALID_INPUT_VALUE);
			} catch (EntityNotFoundException e) {
			    log.error("Entity not found when creating play: {}", e.getMessage());
			    throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND);
			}
		CreatePlayResponse response = new CreatePlayResponse();
		response.setPlayDetail(request.getDetail());
		response.setPlayImage(request.getImage());
		response.setPlayTitle(request.getTitle());

		return response;
	}
//	@Override
//	@Transactional
//	public CreateplayResponse createplay(CreateplayRequest request) {
//	    try {
//	        validateplayDTO(request);
//	        MultipartFile imageFile = request.getImageFile();
//	        
//	        if (imageFile != null && !imageFile.isEmpty()) {
//	            String fileName = "plays/" + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
//	            String imageUrl = fileUploadService.uploadFile(imageFile, fileName);
//	            request.setImage(imageUrl);  // 업로드된 이미지 URL을 request의 image 필드에 저장
//	        }
//
//	        play play = mapJoinDTOToUser(request);
//	        play.setImage(request.getImage());  // play 엔티티에 이미지 URL을 저장
//	        playRepository.save(play);
//
//	    } catch (DataIntegrityViolationException e) {
//	        log.error("Data integrity violation on creating play: {}", e.getMessage());
//	        throw new ServiceException(ErrorCode.INVALID_INPUT_VALUE);
//	    } catch (EntityNotFoundException e) {
//	        log.error("Entity not found when creating play: {}", e.getMessage());
//	        throw new ServiceException(ErrorCode.ENTITY_NOT_FOUND);
//	    } catch (IOException e) {
//	        log.error("Error uploading image to S3: {}", e.getMessage());
//	        throw new RuntimeException("Error uploading image", e);
//	    }
//
//	    CreateplayResponse response = new CreateplayResponse();
//	    response.setplayDetail(request.getDetail());
//	    response.setplayImage(request.getImage());  // 응답에 업로드된 이미지의 URL 포함
//	    response.setplayTitle(request.getTitle());
//
//	    return response;
//	}
	
	private void validatePlayDTO(CreatePlayRequest request) {
	    // 여기에 유효성 검사 로직을 구현, 실패 시 IllegalArgumentException 던지기
	    if (request.getDetail() == null || request.getImage() == null || request.getTitle() == null) {
	        throw new IllegalArgumentException("play detail, image, and title must not be null");
	    }
	}
		
	// 매핑 메서드
	private Play mapJoinDTOToUser(CreatePlayRequest registerplayDTO) {
		Play play = new Play();
		play.setTitle(registerplayDTO.getTitle());
		play.setDetail(registerplayDTO.getDetail());
		play.setImage(registerplayDTO.getImage());

		return play;
	}

	@Override
	public List<Play> getAllPlays() {
		return playRepository.findAll();
	}

	@Override
	public Play getPlayById(Long playIdx) {
		return playRepository.findById(playIdx).orElseThrow(() -> new ServiceException(ErrorCode.PLAY_NOT_FOUND));
	}

	@Override
	@Transactional
	public boolean deletePlayById(Long playIdx) {
		try {
			playRepository.deleteById(playIdx);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public PlayDetailResponse showDetail(Long playId) {
		// 이벤트 찾기
		Play play = playRepository.findById(playId)
				.orElseThrow(() -> new ServiceException(ErrorCode.PLAY_NOT_FOUND));
		// 정보 가져오기 및 반환하기
		PlayDetailResponse response = new PlayDetailResponse();
		response.setPlayId(play.getPlayId());
		response.setTitle(play.getTitle());
		response.setDetail(play.getDetail());
		response.setImage(play.getImage());
		return response;
	}


	@Override
	@Transactional
	public PlayDetailResponse editPlay(Long playId, EditPlayRequest request) {
		Play play = playRepository.findById(playId)
				.orElseThrow(() -> new ServiceException(ErrorCode.PLAY_NOT_FOUND));
		
		play.setTitle(request.getPlayTitle());
		play.setDetail(request.getPlayDetail());
		play.setImage(request.getPlayImage());
		
		
		PlayDetailResponse response = new PlayDetailResponse();
		response.setTitle(play.getTitle());
		response.setDetail(play.getDetail());
		response.setImage(play.getImage());
		return response;
	}

//	@Override
//	public playCreatorListResponse showCreatorList(long playIdx) {
////		// 이벤트 찾기
////		play play = playRepository.findById(playId).orElseThrow();
////		
////		// 크리에이터 꺼내기
////		List<Creator> creator = play.getCreator();
////		   // playCreatorListResponse에 설정
////	    playCreatorListResponse response = new playCreatorListResponse(creator);
//		return playRepository.findCreatorByplayIdx(playIdx)
//				.orElseThrow(() -> new ServiceException(ErrorCode.CREATOR_NOT_FOUND));
//	}

//	@Override
//	public playCreatorListResponse showCreatorList(long playIdx) {
//		// 이벤트 찾기
//		play play = playRepository.findById(playIdx)
//				.orElseThrow(() -> new ServiceException(ErrorCode.play_NOT_FOUND));
//
//		// 이벤트에서 크리에이터 목록 가져오기
//		List<Creator> creators = play.getCreator();
//
//		// playCreatorListResponse에 설정
//		playCreatorListResponse response = new playCreatorListResponse(creators);
//
//		return response;
//	}
//
//	@Override
//	public CreatorDetailResponse showCreatorDetail(long creatorIdx) {
//
//		// 창작자 찾기
//		Creator creator = creatorRepository.findById(creatorIdx)
//				.orElseThrow(() -> new ServiceException(ErrorCode.CREATOR_NOT_FOUND));
//		// 값 매핑하기
//		CreatorDetailResponse response = new CreatorDetailResponse();
//		response.setDepartment(creator.getDepartment());
//		response.setDescription(creator.getDescription());
//		response.setId(creatorIdx);
//		response.setImage(creator.getImage());
//		response.setName(creator.getName());
//
//		return response;
//	}

}
