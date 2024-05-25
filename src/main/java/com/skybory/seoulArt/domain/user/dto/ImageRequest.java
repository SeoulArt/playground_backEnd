package com.skybory.seoulArt.domain.user.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageRequest {

	private MultipartFile image;
}
