package com.skybory.seoulArt.domain.seat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skybory.seoulArt.domain.seat.entity.Seat;


public interface SeatRepository extends JpaRepository<Seat, Long>{
}
