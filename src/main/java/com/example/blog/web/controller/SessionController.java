package com.example.blog.web.controller;

import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionController {
    @GetMapping("oauth2/authorization/google")
//    @GetMapping("/redirect")
    public void callBack(HttpServletResponse response) throws IOException {
        System.out.println("세션 접근");
        response.sendRedirect("https://blog.shin-eunji.com");
    }
}
