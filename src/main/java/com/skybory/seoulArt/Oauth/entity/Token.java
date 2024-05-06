package com.skybory.seoulArt.Oauth.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Token {

	@Id
	private Long token_id;
    private String access_token;
    private String refresh_token;
}
