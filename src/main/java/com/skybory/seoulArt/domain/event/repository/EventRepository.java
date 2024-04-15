package com.skybory.seoulArt.domain.event.repository;



import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;

//import com.skybory.seoulArt.domain.event.dto.EventCreatorListResponse;
import com.skybory.seoulArt.domain.event.dto.EventDescriptionResponse;
import com.skybory.seoulArt.domain.event.entity.Event;


public interface EventRepository extends JpaRepository<Event, Long> {
	
	Optional<EventDescriptionResponse> findEventDeatilByEventIdx(Long eventIdx);

//	Optional<EventCreatorListResponse> findCreatorByEventIdx(Long eventIdx);

}
