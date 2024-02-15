package com.example.blog.web.common.security.service;

import com.example.blog.web.common.security.domain.AuthUser;
import com.example.blog.web.domain.Member;
import com.example.blog.web.dto.OAuthAttributes;
import com.example.blog.web.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private MemberService memberService;

    //social login 이용자 정보 저장
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser");

        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        log.info("loaduser registrationId = " + registrationId);
        log.info("loaduser userNameAttributeName = " + userNameAttributeName);

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        String nameAttributeKey = attributes.getNameAttributeKey();
        String name = attributes.getName();
        String email = attributes.getEmail();
        String picture = attributes.getPicture();
        String id = attributes.getId();
        String socialType = "";

        if ("naver".equals(registrationId)){
            socialType = "naver";
        }else {
            socialType = "google";
        }

        log.info("loaduser nameAttributeKey = "+ nameAttributeKey);
        log.info("loaduser id = " + id);
        log.info("loaduser socialtype = " + socialType);
        log.info("loaduser name = " + name);
        log.info("loaduser email = " + email);
        log.info("loaduser picture = " + picture);

        if (name == null) name = "";
        if (email == null) email = "";

        Member member = null;
        try {
            member = memberService.findBySocial(socialType, id);
        } catch (Exception e) {
            log.info("loadUser e = " + e);
        }

        //신규 이용자의 경우 DB에 저장
        if (member == null) {
            Member newMember = new Member();
            newMember.setUserName(name);
            newMember.setEmail(email);
            newMember.setSocialId(id);
            newMember.setSocialType(socialType);
            newMember.setJob("이용자");
            try {
                memberService.register(newMember);
                member = memberService.findBySocial(socialType, id);
                System.out.println(socialType);
                System.out.println(id);
            } catch (Exception e) {
                log.info("loadUser if e = " + e);
            }
        //기존 이용자의 경우 DB 정보 수정
        } else {
            try {
                if (!name.equals(member.getUserName())) {
                    Member changedMember = new Member();
                    changedMember.setUserName(name);
                    changedMember.setSocialType(socialType);
                    changedMember.setSocialId(id);
                    changedMember.setUpdDate(LocalDateTime.now());
                    memberService.modifyNameBySocial(changedMember);
                }

                if (!email.equals(member.getEmail())) {
                    Member changedMember = new Member();
                    changedMember.setEmail(email);
                    changedMember.setSocialType(socialType);
                    changedMember.setSocialId(id);
                    changedMember.setUpdDate(LocalDateTime.now());
                    memberService.modifyEmailBySocial(changedMember);
                }
            } catch (Exception e) {
                log.info("loadUser else e = " + e);
            }
        }

        log.info("loadUser member = " + member);

        return new AuthUser(member, attributes);
    }
}
