package com.skybory.seoulArt.domain.play.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatePlayResponse {

	private String playTitle;
	private String playDetail;
	private String playImage;
}
