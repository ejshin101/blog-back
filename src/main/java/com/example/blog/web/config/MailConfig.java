package com.example.blog.web.config;

import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {

    @Autowired
    Environment env;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        mailProperties.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
        mailProperties.put("mail.smtp.starttls.required", env.getProperty("mail.smtp.starttls.required"));
        mailProperties.put("mail.smtp.socketFactory.port", env.getProperty("mail.smtp.socketFactory.port"));
        mailProperties.put("mail.smtp.debug", env.getProperty("mail.smtp.debug"));
//        mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailProperties.put("mail.smtp.socketFactory.fallback", env.getProperty("mail.smtp.socketFactory.fallback"));

        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost(env.getProperty("mail.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("mail.port")));
        mailSender.setProtocol(env.getProperty("mail.protocol"));
        mailSender.setUsername(env.getProperty("mail.username"));
        mailSender.setPassword(env.getProperty("mail.password"));
        return mailSender;
    }
}
