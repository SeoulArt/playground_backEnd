package com.skybory.seoulArt.Oauth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenResponse {

	// 네이버, 카카오 엑세스 토큰 반환값
	private String access_token;
	private String refresh_token;
	private int expires_in;
	private int refresh_token_expires_in;
}