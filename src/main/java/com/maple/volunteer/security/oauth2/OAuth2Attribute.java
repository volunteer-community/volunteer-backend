package com.maple.volunteer.security.oauth2;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributes; // 사용자 속성 정보를 담는 Map
    private String attributeKey;
    private String email;
    private String name;
    private String picture;
    private String provider;

    @Builder
    public OAuth2Attribute(Map<String, Object> attributes, String attributeKey, String email, String name, String picture, String provider) {
        this.attributes = attributes;
        this.attributeKey = attributeKey;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.provider = provider;
    }

    static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(provider, attributeKey, attributes);
            case "naver":
                return ofNaver(provider, attributes);
            case "kakao":
                return ofKakao(provider, attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attribute ofGoogle(String provider, String attributeKey, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .email((String) attributes.get("email"))
                .provider(provider)
                .name((String)attributes.get("name"))
                .picture((String)attributes.get("picture"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    private static OAuth2Attribute ofNaver(String provider, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attribute.builder()
                .email((String) response.get("email"))
                .provider(provider)
                .name((String) response.get("name"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .attributeKey("id")
                .build();
    }

    private static OAuth2Attribute ofKakao(String provider, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .email((String) kakaoAccount.get("email"))
                .provider(provider)
                .name((String) kakaoProfile.get("nickname"))
                .picture((String) kakaoProfile.get("profile_image_url"))
                .attributes(kakaoAccount)
                .attributeKey("id")
                .build();
    }

    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("provider", provider);
        map.put("name", name);
        map.put("picture", picture);

        return map;
    }
}