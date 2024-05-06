package com.skybory.seoulArt.Oauth;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Token {

    private String access_token;
    private String refresh_token;
}
