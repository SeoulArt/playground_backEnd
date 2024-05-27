package com.skybory.seoulArt.domain.user.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private Long userId;
	private String username;
	private String role;
	private String profileImage;
	private String phoneNumber;
	private List<Map<String, Long>> ticketPlayList;
	private String description;
	private String playList;
//	private String name;
	private boolean isEditor;
}
