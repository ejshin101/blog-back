package com.example.blog.web.service;

import com.example.blog.web.domain.Mail;

public interface MailService {
    public void sendMail(Mail mail) throws Exception;
}
