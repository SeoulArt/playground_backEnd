package com.skybory.seoulArt.OauthWithReact;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoAccessTokenResponse {

	private String access_token;
	private String refresh_token;
	private int expires_in;
	private int refresh_token_expires_in;
}