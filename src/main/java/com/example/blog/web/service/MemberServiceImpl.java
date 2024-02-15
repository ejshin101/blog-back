package com.example.blog.web.service;

import com.example.blog.web.domain.Member;
import com.example.blog.web.domain.MemberAuth;
import com.example.blog.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public void register(Member request) throws Exception {
        System.out.println("register :: "+request);
        Member member = new Member();
        if (request.getEmail() != null) {
            member.setUserId(request.getEmail());
        }
        member.setUserId(request.getUserId() == null || request.getUserId().equals("") ? "" : request.getUserId());
        member.setUserPw(request.getUserPw() == null || request.getUserPw().equals("") ? "" : request.getUserPw());
        member.setUserName(request.getUserName());
        member.setJob(request.getJob());
        member.setEmail(request.getEmail());
        member.setSocialType(request.getSocialType());
        member.setSocialId(request.getSocialId());

        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setAuth("ROLE_USER");
        member.addAuth(memberAuth);
        memberRepository.save(member);
        request.setUserNo(member.getUserNo());

    }

    @Override
    public Page<Member> findAll(PageRequest pageRequest) throws Exception {
        return memberRepository.findAll(pageRequest);
    }

    @Override
    public Member read(Long userNo) throws Exception {
        return memberRepository.getById(userNo);
    }

    @Override
    public Member findByEmail(String email) throws Exception {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    @Override
    public Member findBySocial(String socialType, String socialId) throws Exception {
        return memberRepository.findBySocial(socialType, socialId);
    }
    @Override
    public void modifyNameBySocial(Member member) throws Exception {
        memberRepository.updateNameBySocial(member.getUserName(), member.getSocialType(), member.getSocialId() , member.getUpdDate());
    }

    @Override
    public void modifyEmailBySocial(Member member) throws Exception {
        memberRepository.updateEmailBySocial(member.getEmail(), member.getSocialType(), member.getSocialId(), member.getUpdDate());
    }

}
