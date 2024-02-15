package com.example.blog.web.service;

import com.example.blog.web.domain.Member;
import com.example.blog.web.dto.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    public void register(Member request) throws Exception;
    public Page<Member> findAll(PageRequest pageRequest) throws Exception;
    @Transactional
    public Member read(Long userNo) throws Exception;
    public Member findByEmail(String email) throws Exception;
    public Member findBySocial(String socialType, String socialId) throws Exception;

    @Transactional
    public void modifyNameBySocial(Member member) throws Exception;
    @Transactional
    public void modifyEmailBySocial(Member member) throws Exception;
}
