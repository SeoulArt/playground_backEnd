package com.skybory.seoulArt.domain.event.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.skybory.seoulArt.domain.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	
}
