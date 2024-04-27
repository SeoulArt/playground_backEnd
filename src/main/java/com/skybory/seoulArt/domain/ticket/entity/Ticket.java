package com.skybory.seoulArt.domain.ticket.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skybory.seoulArt.domain.event.entity.Event;
import com.skybory.seoulArt.domain.seat.entity.Seat;
import com.skybory.seoulArt.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@SequenceGenerator(
		name="TICKET_SEQ_GENERATOR",
		sequenceName = "TICKET_SEQ",
		initialValue = 1, allocationSize = 1
		)
public class Ticket {
	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "TICKET_SEQ_GENERATOR"
			)
	private Long ticketIdx;							// 티켓 고유 넘버
	
	@OneToOne(mappedBy = "ticket" )		// User에 있는 Ticket의 객체명
	private User user;
	
	@OneToOne		// Seat에 있는 Ticket의 객체명
	private Seat seat;
	
	@ManyToOne		// 티켓이 이벤트의 주인이 됨
	@JoinColumn
//	@JsonIgnoreProperties({"ticket"})
	private Event event;
	
	public void changeEvent(Event event) {
		this.event = event;
		event.getTicket().add(this);
	}

	public void setTicketIdx(Long ticketIdx) {
		this.ticketIdx = ticketIdx;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	@Override
	public String toString() {
		return " ";
	}
	
}
