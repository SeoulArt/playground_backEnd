package com.skybory.seoulArt.domain.user.dto;

import com.skybory.seoulArt.global.Dept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatorDetailResponse {

	private String username;
	private Dept department;
	private String image;
	private String description;
	private String profileImage;
}
