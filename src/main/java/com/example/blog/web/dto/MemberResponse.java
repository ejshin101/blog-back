package com.example.blog.web.dto;

import com.example.blog.web.domain.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponse {
    private Long userNo;
    private String userId;
    private String userPw;
    private String userName;
    private String job;
    private LocalDateTime regDate;
    private LocalDateTime updDate;


    public MemberResponse(Member member) {
        this.userNo = member.getUserNo();
        this.userId   = member.getUserId();
        this.userPw   = member.getUserPw();
        this.userName = member.getUserName();
        this.job      = member.getJob();
        this.regDate  = member.getRegDate();
        this.updDate  = member.getUpdDate();
    }
}
