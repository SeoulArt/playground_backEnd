package com.skybory.seoulArt.domain.user.entity;

import java.util.List;

import com.skybory.seoulArt.domain.reply.entity.Reply;
import com.skybory.seoulArt.domain.ticket.entity.Ticket;
import com.skybory.seoulArt.global.Dept;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(
		name="USER_SEQ_GENERATOR",
		sequenceName = "USER,SEQ",
		initialValue = 1, allocationSize = 1
		)
public class User {

	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY,
			generator = "USER_SEQ_GENERATOR"
			)
    private Long id;

    private String username;

    private String email;

    // 역할로는 ROLE_USER , ROLE_ADMIN , ROLE_CREATOR 가 있다.
    private String role;
    
    @OneToOne /*(mappedBy = "user" )*/
	private Ticket ticket;
		
    @OneToMany(mappedBy = "user")
    private List<Reply> replies;
    
    // creator 들만 사용해야함
    private Dept department;	// "작품1" , "작품2", "작품3", 기획팀
    private String profileImage;	// 창작자 프로필 사진
    private String image;		// 창작자 게시글 사진
    private String description;	// 창작자 소개(figma : '너무좋아요' 에 해당)
    
}