//package com.skybory.seoulArt.domain.reply.entity;
//
//import java.time.LocalDateTime;
//
//import com.skybory.seoulArt.domain.play.entity.Play;
//import com.skybory.seoulArt.domain.user.entity.User;
//import com.skybory.seoulArt.global.QnAType;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.SequenceGenerator;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@SequenceGenerator(
//		name="Question_SEQ_GENERATOR",
//		sequenceName = "Question_SEQ",
//		initialValue = 1, allocationSize = 1
//		)
//public class QnA {
//	@Id
//	@GeneratedValue(
//			strategy = GenerationType.IDENTITY,
//			generator = "Question_SEQ_GENERATOR"
//			)
//	private Long questionId;
//	
//	@ManyToOne
//	@JoinColumn(name = "user_Id")
//	private User user;
//	
//	@ManyToOne
//	@JoinColumn(name = "play_Id")
//	private Play play;
//	
//	@OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
//	private Answer answer;
//	
//	private String comment;
//	
//	public void changePlay(Play play) {
//		this.play = play;
//		play.getQuestionist().add(this);
//	}
//	
//	public void changeUser(User user) {
//		this.user = user;
//		user.getQuestionList().add(this);
//	}
//	
//}
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
		name="QNA_SEQ_GENERATOR",
		sequenceName = "QNA_SEQ",
		initialValue = 1, allocationSize = 1
		)
public class QnA {
	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "QNA_SEQ_GENERATOR"
			)
	private Long qnaId;
	
	@ManyToOne
	@JoinColumn(name = "user_Id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "play_Id")
	private Play play;
	
	
	private String questionComment;

	private String answerComment;
	
	
	public void changePlay(Play play) {
		this.play = play;
		play.getQnaList().add(this);
	}
	
	public void changeUser(User user) {
		this.user = user;
		user.getQnaList().add(this);
	}
	
}

