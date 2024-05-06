package com.skybory.seoulArt.Oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "naver")
public class NaverProperties {
    private String requestTokenUri = "https://nid.naver.com/oauth2.0/token";
    private String clientId = "EFSiWGS4Omrh4Tv4ZHgm";
    private String clientSecret = "TpgP7auqTu";

//    public String getRequestURL(String code) {
//        return UriComponentsBuilder.fromHttpUrl(requestTokenUri)
//                .queryParam("grant_type", "authorization_code")
//                .queryParam("client_id", clientId)
//                .queryParam("client_secret", clientSecret)
//                .queryParam("code", code)
//                .toUriString();
//    }
}