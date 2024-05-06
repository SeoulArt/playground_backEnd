package com.skybory.seoulArt.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private Long userId;
	private String username;
	private String role;
	private String profileImage;
//	private String name;
}
