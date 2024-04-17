//package com.skybory.seoulArt.global.OAuth;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Map;
//
//public class CustomOAuth2User implements OAuth2User {
//
//    private final OAuth2Response oAuth2Response;
//    private final String role;
//
//    // role(ADMIN,USER) 을 담아서 만들것이기 때문에, 파라미터 있는 생성자로 받아옴
//    public CustomOAuth2User(OAuth2Response oAuth2Response, String role) {
//
//        this.oAuth2Response = oAuth2Response;
//        this.role = role;
//    }
//
//    @Override
//    public Map<String, Object> getAttributes() {
//
//        return null;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//
//    	// role 값 정의하기
//        Collection<GrantedAuthority> collection = new ArrayList<>();
//
//        collection.add(new GrantedAuthority() {
//
//            @Override
//            public String getAuthority() {
//
//                return role;
//            }
//        });
//
//        return collection;
//    }
//
//    @Override
//    public String getName() {
//
//        return oAuth2Response.getName();
//    }
//
//    // 네이버(또는 카카오) + 제공받은 코드값. ex) 네이버 1013
//    public String getUsername() {
//
//        return oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
//    }
//}