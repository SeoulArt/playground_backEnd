package com.skybory.seoulArt.Oauth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoMemberResponse {
    private long id;
    private String connected_at;
    private Properties properties;
//    private KakaoAccount kakao_account;


}