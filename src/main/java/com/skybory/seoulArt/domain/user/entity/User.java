package com.skybory.seoulArt.domain.user.entity;

import java.util.ArrayList;

import java.util.List;

import com.skybory.seoulArt.domain.reply.entity.QnA;
//import com.skybory.seoulArt.domain.reply.entity.Reply;
import com.skybory.seoulArt.domain.reply.entity.Review;
import com.skybory.seoulArt.domain.ticket.entity.Ticket;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	
//	private String id;	// JWTAuthnticationFilter 때문에 추가했는데, 문제되면 확인해봐야함

//	private String name;
	
    private String username;

    private String email;

    private String verifyCode;
    // 역할로는 ROLE_USER , ROLE_ADMIN , ROLE_CREATOR 가 있다.
    private String role;
    
    @OneToMany(mappedBy = "user")
    private List<Ticket> ticketList = new ArrayList<>();
		
//    @OneToMany(mappedBy = "user")
//    private List<Reply> replyList = new ArrayList<>();
    
    @OneToMany(mappedBy = "user")
    private List<Review> reviewList = new ArrayList<>();
    
    @OneToMany(mappedBy = "user")
    private List<QnA> qnaList = new ArrayList<>();
    
    // creator 들만 사용해야함
    private String department;	// 연출, 배우, 기획, 조명, 무대, 등 등
    private String profileImage;	// 창작자 프로필 사진
    private String creator_image;		// 창작자 게시글 사진
    private String creator_description;	// 창작자 소개(figma : '너무좋아요' 에 해당)
    private String phoneNumber;
    
    private String playList;	// 1번공연, 3번공연, 5번공연
    
    private boolean isEditor;
}