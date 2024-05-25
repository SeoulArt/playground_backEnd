package com.skybory.seoulArt.domain.user.dto;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatorDetailResponse {

	private Long id;
	private String username;
	private String department;
	private String image;
	private String description;
	private String profileImage;
	private String playList;
}
