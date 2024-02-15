package com.example.blog.web.common.security.domain;

import com.example.blog.web.domain.Member;
import com.example.blog.web.dto.OAuthAttributes;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.stream.Collectors;

public class AuthUser extends DefaultOAuth2User {
    private static final long serialVersionUID = 1L;
    private Member member;

    public AuthUser(Member member, OAuthAttributes attributes) {
        super(member.getAuthList().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());

        this.member = member;
    }

    public Long getUserNo() {
        if (member != null) {
            return member.getUserNo();
        }
        return null;
    }

    //회원 정보 조회
    public String getName() {
        if (member != null) {
            return member.getUserName();
        }

        return "";
    }

    public String getUsername() {
        if (member != null) {
            return member.getUserName();
        }
        return "";
    }

    public String getSocialId() {
        if (member != null){
            return member.getSocialId();
        }
        return "";
    }

    public String getSocialType() {
        if (member != null){
            return member.getSocialType();
        }
        return "";
    }

    public String getEmail() {
        if (member != null){
            return member.getEmail();
        }
        return "";
    }

}
