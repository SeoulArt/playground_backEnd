package com.skybory.seoulArt.domain.play.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditPlayRequest {

	private String playTitle;
	private String playDetail;
	private String playImage;
}
