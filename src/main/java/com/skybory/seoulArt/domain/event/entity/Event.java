package com.skybory.seoulArt.domain.event.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skybory.seoulArt.domain.ticket.entity.Ticket;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
		name="EVENT_SEQ_GENERATOR",
		sequenceName = "EVENT,SEQ",
		initialValue = 1, allocationSize = 1
		)
public class Event {

	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "EVENT_SEQ_GENERATOR"
			)
	private Long eventIdx;
	
	@OneToMany(mappedBy = "event")
//	@JsonIgnoreProperties({"event"})
	private List<Ticket> ticket = new ArrayList<>();
	
//	@OneToMany(mappedBy = "event")
//	@JsonIgnoreProperties({"event"})
//	private List<Creator> creator = new ArrayList<>();
	
	private String title;
	private String detail;
	private String image;
	@Override
	public String toString() {
		return " ]";
	}
	
	
}
