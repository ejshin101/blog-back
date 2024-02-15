package com.example.blog.web.controller;

import com.example.blog.web.domain.Mail;
import com.example.blog.web.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MailController {
    private final MailService mailService;

    @PostMapping("/api/sendmail")
    public ResponseEntity<Mail> sendMail(@RequestBody Mail mail) throws Exception {
        mailService.sendMail(mail);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
