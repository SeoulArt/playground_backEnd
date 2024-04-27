package com.skybory.seoulArt.domain.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.skybory.seoulArt.domain.ticket.entity.Ticket;
import com.skybory.seoulArt.domain.user.entity.User;


public interface TicketRepository extends JpaRepository<Ticket, Long>{

	int countByEventEventIdx(long eventIdx);
	
	 Long findTicketIdxByUser(User user);
}
