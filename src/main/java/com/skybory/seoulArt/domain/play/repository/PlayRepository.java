package com.skybory.seoulArt.domain.play.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.skybory.seoulArt.domain.play.entity.Play;

public interface PlayRepository extends JpaRepository<Play, Long> {
	
}
