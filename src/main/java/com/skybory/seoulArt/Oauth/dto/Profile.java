package com.skybory.seoulArt.Oauth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {
    private String nickname;
    private boolean is_default_nickname;
}