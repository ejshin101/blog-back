package com.example.blog.web.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mail {
    private String address;
    private String subject;
    private String content;
    private String name;
}
