package com.skybory.seoulArt.Oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverMemberResponse {
    @JsonProperty("resultcode")
    private String resultCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("response")
    private NaverUserDetail naverUserDetail;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NaverUserDetail {
        private String id;
        private String name;
        private String email;
        private String profile_image;
        private String nickname;
    }
}