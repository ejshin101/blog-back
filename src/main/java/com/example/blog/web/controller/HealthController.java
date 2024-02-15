package com.example.blog.web.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HealthController {
    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "OK";
    }
}
