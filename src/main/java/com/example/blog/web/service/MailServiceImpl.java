package com.example.blog.web.service;

import com.example.blog.web.domain.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    private String adminAddress = "blogeunji@gmail.com";

    @Override
    public void sendMail(Mail mail) throws Exception {
        System.out.println(mail.getAddress());
        System.out.println(mail.getSubject());
        System.out.println(mail.getName());
        System.out.println(mail.getContent());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminAddress);
        message.setSubject(mail.getSubject());
        message.setCc(mail.getAddress());
        message.setText(mail.getName() + "\n"+ mail.getContent());
        javaMailSender.send(message);
    }
}
