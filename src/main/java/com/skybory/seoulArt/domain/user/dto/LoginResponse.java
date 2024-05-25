package com.skybory.seoulArt.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
	   private UserDTO user;
	    private String accessToken;
	    private String refreshToken;
}
