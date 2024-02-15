package com.example.blog.web.controller;

import com.example.blog.web.common.security.domain.AuthUser;
import com.example.blog.web.common.security.domain.CustomUser;
import com.example.blog.web.domain.Member;
import com.example.blog.web.dto.MemberResponse;
import com.example.blog.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<Member> register(@Validated @RequestBody Member request) throws Exception {
        log.info("member.getUserName()="+request.getUserName());

        String inputPassword = request.getUserPw();
        request.setUserPw(passwordEncoder.encode(inputPassword));
        memberService.register(request);
        log.info("register member.getUserNo() = " + request.getUserNo());

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<MemberResponse>> findAll(@RequestParam("page")Integer page, @RequestParam("size") Integer size) throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 9);
        Page<MemberResponse> members = memberService.findAll(pageRequest)
                .map(MemberResponse::new);

        return ResponseEntity.ok()
                .body(members);
    }

    @PostMapping(value = "/setup")
    public ResponseEntity<Member> setupAdmin(@Validated @RequestBody Member request) throws Exception {
        String inputPassword = request.getUserPw();
        request.setUserPw(passwordEncoder.encode(inputPassword));
        memberService.register(request);

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    //회원정보를 가져온다
    @GetMapping("/myinfo")
    public ResponseEntity<Member> getMyInfo(@AuthenticationPrincipal CustomUser customUser) throws Exception {
        System.out.println(customUser);
        Long userNo = customUser.getUserNo();
        System.out.println(userNo);

        log.info("get info userNo = newnewenwenw " + userNo);

        Member member = memberService.read(userNo);
        member.setUserPw("");
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    //회원정보를 가져온다
    @GetMapping("/googleInfo")
    public ResponseEntity<Member> getGoogleInfo(@AuthenticationPrincipal AuthUser AuthUser) throws Exception {
        System.out.println("authuser ::: "+ AuthUser);
        Long userNo = AuthUser.getUserNo();
        System.out.println("userNo ::: " + userNo);
        log.info("get google info userNo = " + userNo);

        Member member = memberService.read(userNo);
        member.setUserPw("");
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

}
