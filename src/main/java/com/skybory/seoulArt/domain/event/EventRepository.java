package com.skybory.seoulArt.domain.event;


import java.util.Optional;

//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skybory.seoulArt.domain.event.dto.EventCreatorListResponse;
import com.skybory.seoulArt.domain.event.dto.EventDescriptionResponse;
import com.skybory.seoulArt.domain.event.entity.Event;


public interface EventRepository extends JpaRepository<Event, Long> {
	
	public Optional<EventDescriptionResponse> getEventDetail(Long id);

	public Optional<EventCreatorListResponse> getCreatorList(long eventId);
}
