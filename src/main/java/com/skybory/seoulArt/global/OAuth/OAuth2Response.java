package com.skybory.seoulArt.global.OAuth;

public interface OAuth2Response {

	// 네이버, 카카오
	String getProvider();
	
	// 제공자가 발급해주는 아이디(번호)
	String getProviderId();
	
	// 이메일
	String getEmail();
	
	// 사용자이름
	String getName();
	
}
