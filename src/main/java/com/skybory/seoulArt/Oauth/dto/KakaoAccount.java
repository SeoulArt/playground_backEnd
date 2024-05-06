package com.skybory.seoulArt.Oauth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoAccount {
    private boolean profile_nickname_needs_agreement;
    private Profile profile;
    private boolean has_email;
    private boolean email_needs_agreement;
    private boolean is_email_valid;
    private boolean is_email_verified;
    private String email;
}