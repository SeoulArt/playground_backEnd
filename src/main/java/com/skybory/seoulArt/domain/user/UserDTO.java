package com.skybory.seoulArt.domain.user;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

	private long id;
	private String email;
	private String role;
	private String username;
}
