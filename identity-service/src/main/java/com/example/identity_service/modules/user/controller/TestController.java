package com.example.identity_service.modules.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/identity")
public class TestController {
    @GetMapping("/ping")
    public String ping() {
        return "Identity Service is running!";
    }
}