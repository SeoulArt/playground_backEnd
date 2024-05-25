package com.skybory.seoulArt.domain.play.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skybory.seoulArt.domain.reply.entity.QnA;
import com.skybory.seoulArt.domain.reply.entity.Review;
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
		name="PLAY_SEQ_GENERATOR",
		sequenceName = "PLAY,SEQ",
		initialValue = 1, allocationSize = 1
		)
public class Play {

	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "PLAY_SEQ_GENERATOR"
			)
	private Long playId;
	
	@OneToMany(mappedBy = "play")
	private List<Ticket> ticket = new ArrayList<>();
	
	@OneToMany(mappedBy = "play")
	private List<Review> reviewList = new ArrayList<>();

	@OneToMany(mappedBy = "play")
	private List<QnA> qnaList = new ArrayList<>();
	
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
