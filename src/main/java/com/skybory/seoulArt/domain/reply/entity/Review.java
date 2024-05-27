package com.skybory.seoulArt.domain.reply.entity;

import com.skybory.seoulArt.domain.play.entity.Play;
import com.skybory.seoulArt.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
		name="REVIEW_SEQ_GENERATOR",
		sequenceName = "REVIEW,SEQ",
		initialValue = 1, allocationSize = 1
		)
public class Review {
	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "REVIEW_SEQ_GENERATOR"
			)
	private Long reviewId;
	
	@ManyToOne
	@JoinColumn(name = "user_Id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "play_Id")
	private Play play;
	
	private String content;
	
	private String image;
	
	private String title;
	
	public void changePlay(Play play) {
		this.play = play;
		play.getReviewList().add(this);
	}
	
	public void changeUser(User user) {
		this.user = user;
		user.getReviewList().add(this);
	}
	
}
