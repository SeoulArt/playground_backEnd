package com.skybory.seoulArt.global.OAuth;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

	// map 선언
	private Map<String, Object> attribute;

	public KakaoResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return attribute.get("id").toString();
	}

	@Override
	public String getEmail() {
		Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
		return (String) properties.get("nickname");
	}

	@Override
	public String getName() {
		Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
		return (String) properties.get("nickname");
	}

}
