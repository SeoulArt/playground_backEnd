package com.skybory.seoulArt.domain.user;

import java.util.List;

import com.skybory.seoulArt.domain.reply.Reply;
import com.skybory.seoulArt.domain.ticket.entity.Ticket;

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

    private String role;
    
	@OneToOne(mappedBy = "user" )
	private Ticket ticket;
		
    @OneToMany(mappedBy = "user")
    private List<Reply> replies;
}