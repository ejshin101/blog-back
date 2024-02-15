package com.example.blog.web.common.security;

import com.example.blog.web.common.security.domain.CustomUser;
import com.example.blog.web.domain.Member;
import com.example.blog.web.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MemberRepository memberRepository;

    //사용자정보 조회
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("userName : " + username);
        Member member = memberRepository.findByUserId(username).get(0);
        log.info("member : " + member);

        return member == null ? null : new CustomUser(member);
    }
}
