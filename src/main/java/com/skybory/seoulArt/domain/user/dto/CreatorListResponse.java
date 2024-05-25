package com.skybory.seoulArt.domain.user.dto;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatorListResponse {

	private Long id;			//1
	private String username;	//홍길동
	private String department;	//배우,연출
//	private String image;
//	private String description;
	private String profileImage;
	private String playList;
}
